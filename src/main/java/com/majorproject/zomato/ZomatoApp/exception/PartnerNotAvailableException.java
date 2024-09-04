package com.majorproject.zomato.ZomatoApp.exception;

public class PartnerNotAvailableException extends RuntimeException{
    public PartnerNotAvailableException() {
    }

    public PartnerNotAvailableException(String message) {
        super(message);
    }
}
