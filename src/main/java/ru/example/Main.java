package ru.example;

import ru.example.back.BackAmountService;
import ru.example.back.BackSystem;
import ru.example.client.Client;
import ru.example.front.FrontalSystem;
import ru.example.handler.RequestHandler;
import ru.example.request.Request;
import ru.example.request.RequestType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        FrontalSystem<Request> frontalSystem = new FrontalSystem<>(2);
        BackSystem backSystem = new BackSystem("Бэк система");
        ExecutorService clientExecutorService = Executors.newFixedThreadPool(1);
        ExecutorService handlerExecutorService = Executors.newFixedThreadPool(2);
        ExecutorService backExecutorService = Executors.newFixedThreadPool(3);
        List<BackAmountService> backAmountServices = new ArrayList<>(3);
        backAmountServices.add(new BackAmountService(5000, 10000, backSystem::setBankAmount));
        backAmountServices.add(new BackAmountService(5000, 1000, backSystem::setBankAmount));
        backAmountServices.add(new BackAmountService(5000, 20000, backSystem::setBankAmount));

        Client client1 = new Client("Клиент1",
                new Request("Клиент1", 10000, RequestType.PAYMENT),
                frontalSystem);
        Client client2 = new Client("Клиент2",
                new Request("Клиент2", 15000, RequestType.PAYMENT),
                frontalSystem);
        Client client3 = new Client("Клиент3",
                new Request("Клиент3", 20000, RequestType.PAYMENT),
                frontalSystem);
        Client client4 = new Client("Клиент4",
                new Request("Клиент4", 5000, RequestType.CREDIT),
                frontalSystem);
        Client client5 = new Client("Клиент5",
                new Request("Клиент5", 150000, RequestType.CREDIT),
                frontalSystem);

        RequestHandler requestHandler1 = new RequestHandler("Обработчик заявок №1", frontalSystem,
                backSystem::processRequest);
        RequestHandler requestHandler2 = new RequestHandler("Обработчик заявок №2", frontalSystem,
                backSystem::processRequest);

        try {
            backExecutorService.invokeAll(backAmountServices);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        clientExecutorService.submit(client1);
        clientExecutorService.submit(client2);
        clientExecutorService.submit(client3);
        clientExecutorService.submit(client4);
        clientExecutorService.submit(client5);
        handlerExecutorService.submit(requestHandler1);
        handlerExecutorService.submit(requestHandler2);
    }
}