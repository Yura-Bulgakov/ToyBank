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
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        FrontalSystem frontalSystem = new FrontalSystem(2);
        BackSystem backSystem = new BackSystem("Бэк система");
        ExecutorService executorService = Executors.newFixedThreadPool(7);
        Supplier<Request> safeRequestSupplier = () -> {
            try {
                return frontalSystem.takeRequest();
            } catch (InterruptedException e) {
                throw new RuntimeException("Не удалось передать заявку обработчику");
            }
        };
        Consumer<Request> safeRequestConsumer = (request) -> {
            try {
                frontalSystem.addRequest(request);
            } catch (InterruptedException e) {
                throw new RuntimeException("Не удалось передать заявку обработчику");
            }
        };

        List<BackAmountService> backAmountServices = new ArrayList<>(3);
        backAmountServices.add(new BackAmountService(5000, backSystem, 10000));
        backAmountServices.add(new BackAmountService(5000, backSystem, 1000));
        backAmountServices.add(new BackAmountService(5000, backSystem, 20000));

        Client client1 = new Client("Клиент1",
                new Request("Клиент1", 10000, RequestType.PAYMENT),
                safeRequestConsumer);
        Client client2 = new Client("Клиент2",
                new Request("Клиент2", 15000, RequestType.PAYMENT),
                safeRequestConsumer);
        Client client3 = new Client("Клиент3",
                new Request("Клиент3", 20000, RequestType.PAYMENT),
                safeRequestConsumer);
        Client client4 = new Client("Клиент4",
                new Request("Клиент4", 5000, RequestType.CREDIT),
                safeRequestConsumer);
        Client client5 = new Client("Клиент5",
                new Request("Клиент5", 150000, RequestType.CREDIT),
                safeRequestConsumer);

        RequestHandler requestHandler1 = new RequestHandler("Обработчик заявок №1", safeRequestSupplier,
                backSystem::processRequest);
        RequestHandler requestHandler2 = new RequestHandler("Обработчик заявок №2", safeRequestSupplier,
                backSystem::processRequest);

        try {
            executorService.invokeAll(backAmountServices);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService.submit(client1);
        executorService.submit(client2);
        executorService.submit(client3);
        executorService.submit(client4);
        executorService.submit(client5);
        executorService.submit(requestHandler1);
        executorService.submit(requestHandler2);
    }
}