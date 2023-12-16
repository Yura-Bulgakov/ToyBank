package ru.example.handler;

import ru.example.request.HandlingRequestDto;
import ru.example.request.Request;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RequestHandler implements Runnable{
    private final String name;
    private final Supplier<Request> requestSupplier;
    private final Consumer<HandlingRequestDto> requestDtoConsumer;

    public RequestHandler(String name, Supplier<Request> requestSupplier,
                          Consumer<HandlingRequestDto> requestDtoConsumer) {
        this.name = name;
        this.requestSupplier = requestSupplier;
        this.requestDtoConsumer = requestDtoConsumer;
    }

    @Override
    public void run() {
        while(true){
            Request handlingRequest = requestSupplier.get();
            System.out.printf("%s: Получена заявка на обработку по клиенту - %s%n",
                    name, handlingRequest.getClientThreadName());
            requestDtoConsumer.accept(new HandlingRequestDto(name,handlingRequest));
        }

    }
}
