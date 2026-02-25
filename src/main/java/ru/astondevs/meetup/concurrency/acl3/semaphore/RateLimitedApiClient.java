package ru.astondevs.meetup.concurrency.acl3.semaphore;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RateLimitedApiClient {
    private final TimedSemaphore rateLimiter;

    public RateLimitedApiClient() {
        this.rateLimiter = TimedSemaphore.builder()
                .setService(Executors.newSingleThreadScheduledExecutor())
                .setLimit(3)
                .setPeriod(5)
                .setTimeUnit(TimeUnit.SECONDS)
                .get();
    }

    public boolean handleRequest(String request) {
        try {
            if (!rateLimiter.tryAcquire()) {
                log.warn("limit exceeded, request {} denied", request);
                return false;
            }

            log.info("initiated processing request {}, available connections {}",
                    request, rateLimiter.getAvailablePermits());
            // Имитация обработки
            Thread.sleep(50);

            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

}
