package ru.example.handler;

import ru.example.request.HandlingRequestDto;
import ru.example.request.Request;
import ru.example.trasfer.Receiver;
import ru.example.trasfer.Sender;

import java.io.IOException;

public class RequestHandler implements Runnable {
    private final String name;
    private final Receiver<Request> requestReceiver;
    private final Sender<HandlingRequestDto> requestDtoSender;

    public RequestHandler(String name, Receiver<Request> requestReceiver,
                          Sender<HandlingRequestDto> requestDtoSender) {
        this.name = name;
        this.requestReceiver = requestReceiver;
        this.requestDtoSender = requestDtoSender;
    }

    @Override
    public void run() {
        while (true) {
            Request handlingRequest = null;
            try {
                handlingRequest = requestReceiver.receive();
                System.out.printf("%s: Получена заявка на обработку по клиенту - %s%n",
                        name, handlingRequest.getClientThreadName());

            } catch (IOException e) {
                throw new RuntimeException("Ошибка при получении заявки от фронтальной системы");
            }
            try {
                requestDtoSender.send(new HandlingRequestDto(name, handlingRequest));
            } catch (IOException e) {
                throw new RuntimeException(String.format("Ошибка при оправке заявки%s в бэк систему", handlingRequest));
            }
        }

    }
}
