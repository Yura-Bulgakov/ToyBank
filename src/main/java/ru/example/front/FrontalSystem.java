package ru.example.front;

import ru.example.trasfer.Receiver;
import ru.example.trasfer.Sender;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FrontalSystem<T> implements Sender<T>, Receiver<T> {
    private final BlockingQueue<T> requestsQueue;

    public FrontalSystem(int capacity) {
        requestsQueue = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public T receive() throws IOException {
        try {
            return requestsQueue.take();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void send(T t) throws IOException {
        try {
            requestsQueue.put(t);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }
}
