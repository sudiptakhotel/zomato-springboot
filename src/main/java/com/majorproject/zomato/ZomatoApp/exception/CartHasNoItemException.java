package com.majorproject.zomato.ZomatoApp.exception;

public class CartHasNoItemException extends RuntimeException{
    public CartHasNoItemException() {
    }

    public CartHasNoItemException(String message) {
        super(message);
    }
}
