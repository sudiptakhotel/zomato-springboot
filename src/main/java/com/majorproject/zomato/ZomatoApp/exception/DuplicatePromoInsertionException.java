package com.majorproject.zomato.ZomatoApp.exception;

public class DuplicatePromoInsertionException extends RuntimeException{
    public DuplicatePromoInsertionException() {
    }

    public DuplicatePromoInsertionException(String message) {
        super(message);
    }
}
