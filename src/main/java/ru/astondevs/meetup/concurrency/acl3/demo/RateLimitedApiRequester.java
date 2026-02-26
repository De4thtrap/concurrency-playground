package ru.astondevs.meetup.concurrency.acl3.demo;

import ru.astondevs.meetup.concurrency.acl3.timedSemaphore.RateLimitedService;

import java.util.ArrayList;
import java.util.List;

public class RateLimitedApiRequester implements ApiRequester {
    private final RateLimitedService service;

    public RateLimitedApiRequester() {
        this.service = new RateLimitedService();
    }

    @Override
    public List<String> collectResponses() throws InterruptedException {
        List<String> responses = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            responses.add(service.getData(String.valueOf(i)));
            Thread.sleep(400);
        }

        return responses;
    }
}
