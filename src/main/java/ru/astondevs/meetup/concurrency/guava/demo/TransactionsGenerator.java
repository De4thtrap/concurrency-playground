package ru.astondevs.meetup.concurrency.guava.demo;

import com.google.common.util.concurrent.*;
import ru.astondevs.meetup.concurrency.guava.listenable.CallbackFactory;
import ru.astondevs.meetup.concurrency.guava.listenable.SimpleTransactionService;
import ru.astondevs.meetup.concurrency.guava.striped.StripedTransactionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TransactionsGenerator {

    private final Random random = new Random();
    private final TransactionService stripedTransactionService = new StripedTransactionService();
    private final TransactionService simpleTransactionService = new SimpleTransactionService();

    private final ThreadFactory stripedFactory = new ThreadFactoryBuilder()
            .setNameFormat("striped-%d")
            .build();

    private final ThreadFactory listenableFactory = new ThreadFactoryBuilder()
            .setNameFormat("listenable-%d")
            .build();

    private final ThreadFactory auditFactory = new ThreadFactoryBuilder()
            .setNameFormat("audit-%d")
            .build();

    private final ThreadFactory monitoringFactory = new ThreadFactoryBuilder()
            .setNameFormat("monitoring-%d")
            .build();

    public void generateStripedTransactions() throws InterruptedException {
        List<String> accounts = generateAccounts();

        try (ExecutorService executorService = Executors.newCachedThreadPool(stripedFactory)) {
            var tasks = accounts.stream().map(accountId -> prepareTask(accountId, stripedTransactionService)).toList();
            executorService.invokeAll(tasks);
        }

        System.out.printf("Transaction report :" + stripedTransactionService.getAllData());
    }

    public void generateListenableTransactions() throws InterruptedException {
        List<String> accounts = generateAccounts();

        var auditExecutorService = Executors.newFixedThreadPool(3, auditFactory);
        var monitoringExecutorService = Executors.newFixedThreadPool(3, monitoringFactory);

        try (ListeningExecutorService executorService =
                     MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10, listenableFactory))) {
            var tasks = accounts.stream().map(accountId -> prepareTask(accountId, simpleTransactionService)).toList();
            for (var task : tasks) {
                var future = executorService.submit(task);
                Futures.addCallback(future, CallbackFactory.sendAuditEvent, auditExecutorService);
                Futures.addCallback(future, CallbackFactory.sendMonitoringEvent, monitoringExecutorService);
            }
        } finally {
            auditExecutorService.shutdown();
            monitoringExecutorService.shutdown();
        }

        auditExecutorService.awaitTermination(1, TimeUnit.SECONDS);
        monitoringExecutorService.awaitTermination(1, TimeUnit.SECONDS);

        System.out.printf("Transaction report: " + simpleTransactionService.getAllData());
    }

    private Callable<BigDecimal> prepareTask(String accountId, TransactionService transactionService) {
        var diff = random.nextInt() % 10000 / 100.0;
        return () -> transactionService.processTransaction(accountId, BigDecimal.valueOf(diff));
    }

    private List<String> generateAccounts() {
        List<String> accounts = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            accounts.add("account#" + Math.abs(random.nextInt()) % 10);
        }
        return accounts;
    }
}
