package ru.example.trasfer;

import java.io.IOException;

@FunctionalInterface
public interface Receiver<T> {
    T receive() throws IOException;
}
