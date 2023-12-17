package ru.example.front;

import ru.example.request.Request;

import java.util.concurrent.ArrayBlockingQueue;

public class FrontalSystem {
    private final ArrayBlockingQueue<Request> requestsQueue;

    public FrontalSystem(int capacity) {
        requestsQueue = new ArrayBlockingQueue<>(capacity);
    }

    public void addRequest(Request request) throws InterruptedException {
        requestsQueue.put(request);
    }

    public Request takeRequest() throws InterruptedException {
        return requestsQueue.take();
    }
}
