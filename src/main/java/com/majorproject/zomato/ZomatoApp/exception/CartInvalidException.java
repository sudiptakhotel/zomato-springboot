package com.majorproject.zomato.ZomatoApp.exception;

public class CartInvalidException extends RuntimeException{
    public CartInvalidException() {
    }

    public CartInvalidException(String message) {
        super(message);
    }
}
