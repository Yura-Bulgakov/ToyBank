package ru.example.trasfer;

import java.io.IOException;

@FunctionalInterface
public interface Sender<T> {
    void send(T t) throws IOException;
}
