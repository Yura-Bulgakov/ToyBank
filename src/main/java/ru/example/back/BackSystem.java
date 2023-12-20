package ru.example.back;

import ru.example.request.HandlingRequestDto;

import java.util.concurrent.atomic.AtomicLong;

public class BackSystem {
    private final String name;
    private AtomicLong bankAmount;

    public BackSystem(String name) {
        this.name = name;
        this.bankAmount = new AtomicLong(0);
    }

    public AtomicLong getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(int bankAmount) {
        this.bankAmount.getAndUpdate((x) -> x + bankAmount);
    }

    public void processRequest(HandlingRequestDto dto) {
        switch (dto.getRequest().getRequestType()) {
            case CREDIT: {
                handleCredit(dto);
                break;
            }
            case PAYMENT: {
                handlePayment(dto);
                break;
            }
        }
    }

    private void handlePayment(HandlingRequestDto dto) {
        bankAmount.getAndUpdate((amount) -> amount + dto.getRequest().getAmount());
        System.out.printf("%s: Заявка: %s УСПЕШНО ВЫПОЛНЕНА. Получена от: %s. Баланс банка: %d%n",
                name, dto.getRequest(), dto.getCallerName(), bankAmount.get());
    }

    private void handleCredit(HandlingRequestDto dto) {
        if (bankAmount.get() - dto.getRequest().getAmount() < 0) {
            System.out.printf("%s: Заявка: %s НЕ ВЫПОЛНЕНА. Получена от: %s. Сумма больше баланса банка." +
                    " Баланс банка: %d%n", name, dto.getRequest(), dto.getCallerName(), bankAmount.get());
            return;
        }
        bankAmount.getAndUpdate((amount) -> amount - dto.getRequest().getAmount());
        System.out.printf("%s: Заявка: %s УСПЕШНО ВЫПОЛНЕНА. Получена от: %s. Баланс банка: %d%n",
                name, dto.getRequest(), dto.getCallerName(), bankAmount.get());
    }


}
