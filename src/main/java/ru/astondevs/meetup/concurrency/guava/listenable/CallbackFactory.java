package ru.astondevs.meetup.concurrency.guava.listenable;

import com.google.common.util.concurrent.FutureCallback;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class CallbackFactory {

    private static final AtomicLong successCount = new AtomicLong(0);
    private static final AtomicLong failureCount = new AtomicLong(0);

    public static final FutureCallback<BigDecimal> sendAuditEvent =
            new FutureCallback<>() {
                @Override
                public void onSuccess(BigDecimal result) {
                    System.out.printf("%s [audit message] success event: %s$ transaction\n",
                            Thread.currentThread().getName(), result);
                }

                @Override
                public void onFailure(Throwable t) {
                    System.out.printf("%s [audit message] error event: %s\n",
                            Thread.currentThread().getName(), t.getMessage());
                }
            };

    public static final FutureCallback<BigDecimal> sendMonitoringEvent =
            new FutureCallback<>() {
                @Override
                public void onSuccess(BigDecimal result) {
                    System.out.printf("%s [monitoring message] success event: %d\n",
                            Thread.currentThread().getName(), successCount.incrementAndGet());
                }

                @Override
                public void onFailure(Throwable t) {
                    System.out.printf("%s [monitoring message] error event: %d\n",
                            Thread.currentThread().getName(), failureCount.incrementAndGet());
                }
            };
}
