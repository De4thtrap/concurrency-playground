package ru.astondevs.meetup.concurrency.guava.demo;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import ru.astondevs.meetup.concurrency.guava.striped.AccountManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TransactionsGenerator {

    private final Random random = new Random();
    private final AccountManager accountManager = new AccountManager();

    private final ThreadFactory factory = new ThreadFactoryBuilder()
            .setNameFormat("stripped-pool-%d")
            .build();

    public void generateTransactions() throws InterruptedException {
        List<String> accounts = generateAccounts();

        try (ExecutorService executorService = Executors.newCachedThreadPool(factory)) {
            var tasks = accounts.stream().map(this::prepareTask).toList();
            executorService.invokeAll(tasks);
        }

        System.out.println(accountManager.getUserAccounts().values());
    }

    private Callable<BigDecimal> prepareTask(String accountId) {
        var diff = random.nextInt() % 10000 / 100.0;
        return () -> accountManager.processTransaction(accountId, BigDecimal.valueOf(diff));
    }

    private List<String> generateAccounts() {
        List<String> accounts = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            accounts.add("account#" + Math.abs(random.nextInt()) % 10);
        }
        return accounts;
    }
}
