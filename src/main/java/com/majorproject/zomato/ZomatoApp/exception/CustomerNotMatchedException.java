package com.majorproject.zomato.ZomatoApp.exception;

public class CustomerNotMatchedException extends RuntimeException{
    public CustomerNotMatchedException() {
    }

    public CustomerNotMatchedException(String message) {
        super(message);
    }
}
