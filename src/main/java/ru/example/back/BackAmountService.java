package ru.example.back;

import java.util.concurrent.Callable;

public class BackAmountService implements Callable<Boolean> {
    private final int processTime;
    private final BackSystem backSystem;
    private final int gettingAmount;

    public BackAmountService(int processTime, BackSystem backSystem, int gettingAmount) {
        this.processTime = processTime;
        this.backSystem = backSystem;
        this.gettingAmount = gettingAmount;
    }

    @Override
    public Boolean call() throws Exception {
        Thread.sleep(processTime);
        backSystem.setBankAmount(gettingAmount);
        return true;
    }
}
