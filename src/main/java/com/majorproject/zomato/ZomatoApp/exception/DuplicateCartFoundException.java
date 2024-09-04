package com.majorproject.zomato.ZomatoApp.exception;

public class DuplicateCartFoundException extends RuntimeException{
    public DuplicateCartFoundException() {
    }

    public DuplicateCartFoundException(String message) {
        super(message);
    }
}
