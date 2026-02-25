package ru.astondevs.meetup.concurrency.acl3.demo;

import ru.astondevs.meetup.concurrency.acl3.semaphore.RateLimitedApiClient;

import java.util.ArrayList;
import java.util.List;

public class RateLimitedApiRequester {
    private final RateLimitedApiClient apiClient;

    public RateLimitedApiRequester() {
        this.apiClient = new RateLimitedApiClient();
    }


    public List<Boolean> collectResponses() throws InterruptedException {
        List<Boolean> responses = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            responses.add(apiClient.handleRequest(String.valueOf(i)));
            Thread.sleep(400);
        }

        return responses;
    }
}
