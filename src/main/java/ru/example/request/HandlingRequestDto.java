package ru.example.request;

public class HandlingRequestDto {
    private final String callerName;
    private final Request request;

    public HandlingRequestDto(String callerName, Request request) {
        this.callerName = callerName;
        this.request = request;
    }

    public String getCallerName() {
        return callerName;
    }

    public Request getRequest() {
        return request;
    }
}
