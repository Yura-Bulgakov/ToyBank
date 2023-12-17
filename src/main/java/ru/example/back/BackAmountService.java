package ru.example.back;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BackAmountService implements Callable<Void> {
    private final int processTime;
    private final int gettingAmount;
    private final Consumer<Integer> backSystemAmountUpper;

    public BackAmountService(int processTime, int gettingAmount, Consumer<Integer> backSystemAmountUpper) {
        this.processTime = processTime;
        this.gettingAmount = gettingAmount;
        this.backSystemAmountUpper = backSystemAmountUpper;
    }

    @Override
    public Void call() throws Exception {
        Thread.sleep(processTime);
        backSystemAmountUpper.accept(gettingAmount);
        return null;
    }
}
