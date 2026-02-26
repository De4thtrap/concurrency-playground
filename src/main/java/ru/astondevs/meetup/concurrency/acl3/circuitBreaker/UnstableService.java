package ru.astondevs.meetup.concurrency.acl3.circuitBreaker;

import ru.astondevs.meetup.concurrency.acl3.demo.DemoService;

import java.util.Random;

public class UnstableService implements DemoService {
    private final Random random = new Random();

    @Override
    public String getData(String request) throws InterruptedException {
        var multiplier = random.nextInt() % 10;
        Thread.sleep(100 * multiplier);

        if (multiplier > 5) {
            throw new RuntimeException("Timeout");
        }

        return "Response";
    }
}
