package com.majorproject.zomato.ZomatoApp.exception;

public class InvalidPartnerException extends RuntimeException{
    public InvalidPartnerException() {
    }

    public InvalidPartnerException(String message) {
        super(message);
    }
}
