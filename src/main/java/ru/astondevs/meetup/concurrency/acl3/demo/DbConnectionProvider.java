package ru.astondevs.meetup.concurrency.acl3.demo;

import org.apache.commons.lang3.concurrent.ConcurrentException;
import ru.astondevs.meetup.concurrency.acl3.init.DbConnectionInitializer;

import java.sql.Connection;

public class DbConnectionProvider {
    private final DbConnectionInitializer connectionInitializer;

    public DbConnectionProvider() {
        connectionInitializer = new DbConnectionInitializer();
        connectionInitializer.start();
    }

    public Connection getConnection() {
        try {
            return connectionInitializer.get();
        } catch (ConcurrentException e) {
            throw new RuntimeException(e);
        }
    }
}
