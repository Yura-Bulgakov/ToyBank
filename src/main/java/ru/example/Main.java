package ru.example;

import ru.example.back.BackSystem;
import ru.example.client.Client;
import ru.example.front.FrontalSystem;
import ru.example.handler.RequestHandler;
import ru.example.request.Request;
import ru.example.request.RequestType;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        FrontalSystem frontalSystem = new FrontalSystem(2);
        BackSystem backSystem = new BackSystem("Бэк система", 0);
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
        Thread client1 = new Thread(new Client("Клиент1",
                new Request("Клиент1", 10000, RequestType.PAYMENT),
                safeRequestConsumer));
        Thread client2 = new Thread(new Client("Клиент2",
                new Request("Клиент2", 15000, RequestType.PAYMENT),
                safeRequestConsumer));
        Thread client3 = new Thread(new Client("Клиент3",
                new Request("Клиент3", 20000, RequestType.PAYMENT),
                safeRequestConsumer));
        Thread client4 = new Thread(new Client("Клиент4",
                new Request("Клиент4", 5000, RequestType.CREDIT),
                safeRequestConsumer));
        Thread client5 = new Thread(new Client("Клиент5",
                new Request("Клиент5", 150000, RequestType.CREDIT),
                safeRequestConsumer));

        Thread requestHandler1 = new Thread(new RequestHandler("Обработчик заявок №1", safeRequestSupplier,
                backSystem::processRequest));
        Thread requestHandler2 = new Thread(new RequestHandler("Обработчик заявок №2", safeRequestSupplier,
                backSystem::processRequest));
        client1.start();
        client2.start();
        client3.start();
        client4.start();
        client5.start();
        requestHandler1.start();
        requestHandler2.start();
    }
}