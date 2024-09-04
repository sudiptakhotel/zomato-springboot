package com.majorproject.zomato.ZomatoApp.exception;

public class InvalidOrderStatusException extends RuntimeException{
    public InvalidOrderStatusException() {
    }

    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
