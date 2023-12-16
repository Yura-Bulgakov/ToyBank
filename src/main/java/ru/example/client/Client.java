package ru.example.client;

import ru.example.request.Request;

import java.util.function.Consumer;

public class Client implements Runnable {
    private final String name;
    private final Request request;
    private final Consumer<Request> requestConsumer;


    public Client(String name, Request request, Consumer<Request> requestConsumer) {
        this.name = name;
        this.request = request;
        this.requestConsumer = requestConsumer;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        System.out.printf("%s: Заявка%s отправлена в банк%n", name, request);
        requestConsumer.accept(request);
    }
}
