package ru.astondevs.meetup.concurrency.acl3.circuitBreaker;

import java.util.Random;

public class UnstableService {
    public String getData() throws InterruptedException {
        Random random = new Random();
        var multiplier = random.nextInt() % 10;
        Thread.sleep(1000 * multiplier);

        if (multiplier > 5) {
            throw new RuntimeException("Timeout");
        }

        return "Response";
    }
}
