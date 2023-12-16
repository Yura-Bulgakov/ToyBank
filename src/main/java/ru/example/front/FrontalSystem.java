package ru.example.front;

import ru.example.request.Request;

import java.util.ArrayDeque;
import java.util.Queue;

public class FrontalSystem {
    private final Queue<Request> requestsQueue;
    private final int capacity;

    public FrontalSystem(int capacity) {
        this.capacity = capacity;
        requestsQueue = new ArrayDeque<>();
    }

    public synchronized void addRequest(Request request) throws InterruptedException {
        while (requestsQueue.size() >= capacity) {
            wait();
        }
        requestsQueue.add(request);
        notifyAll();
    }


    public synchronized Request takeRequest() throws InterruptedException {
        while (requestsQueue.size() < 1) {
            wait();
        }
        var returnRequest = requestsQueue.poll();
        notifyAll();
        return returnRequest;
    }
}
