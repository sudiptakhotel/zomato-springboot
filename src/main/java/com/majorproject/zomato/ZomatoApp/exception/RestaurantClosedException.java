package com.majorproject.zomato.ZomatoApp.exception;

public class RestaurantClosedException extends RuntimeException{
    public RestaurantClosedException() {
    }

    public RestaurantClosedException(String message) {
        super(message);
    }
}
