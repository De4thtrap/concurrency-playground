package ru.astondevs.meetup.concurrency.guava.striped;

import com.google.common.util.concurrent.Striped;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

@Slf4j
public class AccountManager {

    // Рекомендуется задавать размер равный степени двойки
    private final Striped<Lock> stripedLocks = Striped.lazyWeakLock(64);

    /**
     * Имитация БД
     */
    @Getter
    private final ConcurrentHashMap<String, UserAccount> userAccounts = new ConcurrentHashMap<>();

    private final Random random = new Random();

    public BigDecimal processTransaction(String accountId, BigDecimal diff) {
        Lock lock = stripedLocks.get(accountId);
        lock.lock();

        try {
            UserAccount account = userAccounts.computeIfAbsent(accountId, UserAccount::new);
            log.debug("processing {}: transaction amount = {}", accountId, diff);
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
}