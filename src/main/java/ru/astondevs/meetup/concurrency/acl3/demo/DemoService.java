package ru.astondevs.meetup.concurrency.acl3.demo;

public interface DemoService {
    String getData(String request) throws InterruptedException;
}
