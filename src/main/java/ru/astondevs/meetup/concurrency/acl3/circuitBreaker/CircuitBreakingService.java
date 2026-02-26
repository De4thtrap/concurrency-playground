package ru.astondevs.meetup.concurrency.acl3.circuitBreaker;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;
import ru.astondevs.meetup.concurrency.acl3.demo.DemoService;

import java.util.concurrent.TimeUnit;

/**
 * Proxy for {@link UnstableService}
 */
@Slf4j
public class CircuitBreakingService implements DemoService {
    private final EventCountCircuitBreaker circuitBreaker =
            new EventCountCircuitBreaker(5, 1, TimeUnit.MINUTES);

    private final UnstableService unstableService;

    public CircuitBreakingService() {
        this.unstableService = new UnstableService();
    }

    @Override
    public String getData(String request) {
        if (!circuitBreaker.checkState()) {
            log.warn("CircuitBreaker is open! Service calls are stopped.");
            return "DefaultResponse";
        }

        log.info("CircuitBreaker is closed. Calling service.");
        try {
            return unstableService.getData(request);
        } catch (Exception e) {
            circuitBreaker.incrementAndCheckState();
            return "Timeout";
        }

    }
}
