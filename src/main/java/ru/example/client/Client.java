package ru.example.client;

import ru.example.request.Request;
import ru.example.trasfer.Sender;

import java.io.IOException;

public class Client implements Runnable {
    private final String name;
    private final Request request;
    private final Sender<Request> requestSender;


    public Client(String name, Request request, Sender<Request> requestSender) {
        this.name = name;
        this.request = request;
        this.requestSender = requestSender;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            requestSender.send(request);
            System.out.printf("%s: Заявка%s отправлена в банк%n", name, request);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке заявки");
        }
    }
}
