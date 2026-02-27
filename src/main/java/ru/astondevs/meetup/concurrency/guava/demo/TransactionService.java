package ru.astondevs.meetup.concurrency.guava.demo;

import java.math.BigDecimal;
import java.util.Map;

public interface TransactionService {
    BigDecimal processTransaction(String accountId, BigDecimal diff);

    Map<String, BigDecimal> getAllData();
}
