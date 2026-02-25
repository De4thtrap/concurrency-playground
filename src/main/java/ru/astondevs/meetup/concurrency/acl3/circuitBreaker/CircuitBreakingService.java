package ru.astondevs.meetup.concurrency.acl3.circuitBreaker;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;

import java.util.concurrent.TimeUnit;

/**
 * Proxy for {@link UnstableService}
 */
@Slf4j
public class CircuitBreakingService {
    private final EventCountCircuitBreaker circuitBreaker =
            new EventCountCircuitBreaker(5, 1, TimeUnit.MINUTES);

    private final UnstableService unstableService;

    public CircuitBreakingService() {
        this.unstableService = new UnstableService();
    }

    public String getData() {
        if (circuitBreaker.checkState()) {
            log.info("CircuitBreaker is closed");
            try {
                return unstableService.getData();
            } catch (Exception e) {
                circuitBreaker.incrementAndCheckState();
                return "Timeout";
            }
        } else {
            log.warn("CircuitBreaker is open!");
        }

        return "DefaultResponse";
    }
}
