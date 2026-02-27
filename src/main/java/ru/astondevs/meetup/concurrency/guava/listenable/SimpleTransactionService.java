package ru.astondevs.meetup.concurrency.guava.listenable;

import lombok.extern.slf4j.Slf4j;
import ru.astondevs.meetup.concurrency.guava.demo.TransactionService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class SimpleTransactionService implements TransactionService {

    private final Map<String, BigDecimal> userAccounts = new ConcurrentHashMap<>();

    @Override
    public BigDecimal processTransaction(String accountId, BigDecimal diff) {
        log.debug("processing {}: transaction amount = {}", accountId, diff);
        var balance = userAccounts.putIfAbsent(accountId, diff);
        if (balance == null) {
            log.info("transaction on NEW account {} have been successfully processed! updated balance: {}",
                    accountId, diff);
            return diff;
        }

        userAccounts.replace(accountId, balance.add(diff));
        var updatedBalance = userAccounts.get(accountId);

        log.info("transaction on {} have been successfully processed! updated balance: {}",
                accountId, updatedBalance);
        return updatedBalance;
    }

    @Override
    public Map<String, BigDecimal> getAllData() {
        return userAccounts;
    }
}