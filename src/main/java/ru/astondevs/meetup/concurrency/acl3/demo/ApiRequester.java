package ru.astondevs.meetup.concurrency.acl3.demo;

import java.util.List;

public interface ApiRequester {
    List<String> collectResponses() throws InterruptedException;
}
