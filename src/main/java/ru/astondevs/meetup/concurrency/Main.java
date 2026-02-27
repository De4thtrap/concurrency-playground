package ru.astondevs.meetup.concurrency;

import ru.astondevs.meetup.concurrency.acl3.demo.ApiRequester;
import ru.astondevs.meetup.concurrency.acl3.demo.DbConnectionProvider;
import ru.astondevs.meetup.concurrency.acl3.demo.RateLimitedApiRequester;
import ru.astondevs.meetup.concurrency.acl3.demo.UnstableApiRequester;
import ru.astondevs.meetup.concurrency.guava.demo.TransactionsGenerator;

public class Main {
    public static void main(String[] args) throws Exception {
        var connectionProvider = new DbConnectionProvider();
        System.out.println(connectionProvider.getConnection().isClosed());

        ApiRequester unstableRequester = new UnstableApiRequester();
        System.out.println(unstableRequester.collectResponses());

        ApiRequester limitedRequester = new RateLimitedApiRequester();
        System.out.println(limitedRequester.collectResponses());

        var transactionGenerator = new TransactionsGenerator();
        transactionGenerator.generateStripedTransactions();
        transactionGenerator.generateListenableTransactions();
    }
}
