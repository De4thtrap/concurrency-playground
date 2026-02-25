package ru.astondevs.meetup.concurrency;

import ru.astondevs.meetup.concurrency.acl3.demo.DbConnectionProvider;
import ru.astondevs.meetup.concurrency.acl3.demo.RateLimitedApiRequester;
import ru.astondevs.meetup.concurrency.acl3.demo.UnstableApiRequester;

public class Main {
    public static void main(String[] args) throws Exception {
        var connectionProvider = new DbConnectionProvider();
        System.out.println(connectionProvider.getConnection().isClosed());

        var unstableRequester = new UnstableApiRequester();
        System.out.println(unstableRequester.collectResponses());

        var limitedRequester = new RateLimitedApiRequester();
        System.out.println(limitedRequester.collectResponses());
    }
}
