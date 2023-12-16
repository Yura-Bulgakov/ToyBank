package ru.example.back;

import ru.example.request.HandlingRequestDto;
import ru.example.request.RequestType;

public class BackSystem {
    private final String name;
    private int bankAmount;

    public BackSystem(String name, int initialBankAmount) {
        this.name = name;
        this.bankAmount = initialBankAmount;
    }

    public int getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(int bankAmount) {
        this.bankAmount = bankAmount;
    }

    public synchronized void processRequest(HandlingRequestDto dto){
        RequestType type = dto.getRequest().getRequestType();
        switch (type){
            case CREDIT:{
                handleCredit(dto);
                break;
            }
            case PAYMENT:{
                handlePayment(dto);
                break;
            }
        }
    }

    private void handlePayment(HandlingRequestDto dto){
        bankAmount += dto.getRequest().getAmount();
        System.out.printf("%s: Заявка: %s УСПЕШНО ВЫПОЛНЕНА. Получена от: %s. Баланс банка: %d%n",
                name, dto.getRequest(), dto.getCallerName(), bankAmount);
    }

    private void handleCredit(HandlingRequestDto dto){
        if (bankAmount - dto.getRequest().getAmount() < 0){
            System.out.printf("%s: Заявка: %s НЕ ВЫПОЛНЕНА. Получена от: %s. Сумма больше баланса банка." +
                            " Баланс банка: %d%n", name, dto.getRequest(), dto.getCallerName(), bankAmount);
            return;
        }
        bankAmount -= dto.getRequest().getAmount();
        System.out.printf("%s: Заявка: %s УСПЕШНО ВЫПОЛНЕНА. Получена от: %s. Баланс банка: %d%n",
                name, dto.getRequest(), dto.getCallerName(), bankAmount);
    }


}
