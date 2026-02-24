package ru.astondevs.meetup.concurrency;

import ru.astondevs.meetup.concurrency.acl3.demo.DbConnectionProvider;

public class Main {
    public static void main(String[] args) throws Exception {
        var connectionProvider = new DbConnectionProvider();
        System.out.println(connectionProvider.getConnection().isClosed());
    }
}
