package ru.astondevs.meetup.concurrency.acl3.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BackgroundInitializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.Executors;

@Slf4j
public class DbConnectionInitializer extends BackgroundInitializer<Connection> {
    private static final Map<String, String> DEFAULT_PROPS = Map.of(
            "url", "jdbc:postgresql://localhost:5432/playground_db?currentSchema=playground",
            "username", "postgres",
            "password", "postgres"
    );
    private final Map<String, String> properties;

    public DbConnectionInitializer() {
        super(Executors.newSingleThreadExecutor());
        this.properties = DEFAULT_PROPS;
    }

    public DbConnectionInitializer(Map<String, String> properties) {
        super(Executors.newSingleThreadExecutor());
        this.properties = properties;
    }

    @Override
    protected Connection initialize() throws Exception {
        log.debug("establishing connection to database");

        var url = properties.get("url");
        var username = properties.get("username");
        var password = properties.get("password");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            log.debug("connection established");
            return connection;
        } catch (SQLException e) {
            log.debug("connection error: {}", e.getMessage());
            throw e;
        }
    }
}
