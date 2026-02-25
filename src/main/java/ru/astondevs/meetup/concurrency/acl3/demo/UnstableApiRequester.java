package ru.astondevs.meetup.concurrency.acl3.demo;

import ru.astondevs.meetup.concurrency.acl3.circuitBreaker.CircuitBreakingService;

import java.util.ArrayList;
import java.util.List;

public class UnstableApiRequester {
    private final CircuitBreakingService service;

    public UnstableApiRequester() {
        this.service = new CircuitBreakingService();
    }

    public List<String> collectResponses() {
        List<String> responses = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            responses.add(service.getData());
        }

        return responses;
    }
}
