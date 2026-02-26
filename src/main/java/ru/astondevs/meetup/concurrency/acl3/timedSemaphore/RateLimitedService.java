package ru.astondevs.meetup.concurrency.acl3.timedSemaphore;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import ru.astondevs.meetup.concurrency.acl3.demo.DemoService;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RateLimitedService implements DemoService {
    private final TimedSemaphore rateLimiter;

    public RateLimitedService() {
        this.rateLimiter = TimedSemaphore.builder()
                .setService(Executors.newSingleThreadScheduledExecutor())
                .setLimit(3)
                .setPeriod(5)
                .setTimeUnit(TimeUnit.SECONDS)
                .get();
    }

    @Override
    public String getData(String request) throws InterruptedException {
        if (!rateLimiter.tryAcquire()) {
            log.warn("limit exceeded, request {} denied", request);
            return "not processed";
        }

        log.info("initiated processing request {}, available connections {}",
                request, rateLimiter.getAvailablePermits());
        // Имитация обработки
        Thread.sleep(50);

        return "processed";
    }

}
