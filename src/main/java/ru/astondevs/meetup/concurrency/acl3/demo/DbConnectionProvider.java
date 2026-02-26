package ru.astondevs.meetup.concurrency.acl3.demo;

import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import ru.astondevs.meetup.concurrency.acl3.concurrentInitializer.DbConnectionInitializer;

import java.sql.Connection;

public class DbConnectionProvider {
    private final DbConnectionInitializer connectionInitializer;

    public DbConnectionProvider() {
        connectionInitializer = new DbConnectionInitializer();
        connectionInitializer.start();
    }

    public Connection getConnection() {
        return ConcurrentUtils.initializeUnchecked(connectionInitializer);
    }
}
