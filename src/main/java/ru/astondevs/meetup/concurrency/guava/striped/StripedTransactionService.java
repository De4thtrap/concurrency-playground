package ru.astondevs.meetup.concurrency.guava.striped;

import com.google.common.util.concurrent.Striped;
import lombok.extern.slf4j.Slf4j;
import ru.astondevs.meetup.concurrency.guava.demo.TransactionService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Slf4j
public class StripedTransactionService implements TransactionService {

    // Рекомендуется задавать размер равный степени двойки
    private final Striped<Lock> stripedLocks = Striped.lazyWeakLock(64);

    /**
     * Имитация БД
     */
    private final Map<String, UserAccount> userAccounts = new HashMap<>();

    private final Random random = new Random();

    @Override
    public BigDecimal processTransaction(String accountId, BigDecimal diff) {
        log.debug("processing {}: transaction amount = {}", accountId, diff);
        Lock lock = stripedLocks.get(accountId);
        lock.lock();

        try {
            UserAccount account = userAccounts.computeIfAbsent(accountId, UserAccount::new);
            var updatedBalance = account.changeBalance(diff);

//            Имитация обработки
            var multiplier = random.nextInt() % 10;
            Thread.sleep(100 * multiplier);
            log.info("transaction on {} have been successfully processed! updated balance: {}",
                    accountId, updatedBalance);

            return updatedBalance;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<String, BigDecimal> getAllData() {
        return userAccounts.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getBalance()));
    }
}