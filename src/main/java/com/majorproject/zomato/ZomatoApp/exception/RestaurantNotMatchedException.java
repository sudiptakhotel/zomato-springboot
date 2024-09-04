package com.majorproject.zomato.ZomatoApp.exception;

public class RestaurantNotMatchedException extends RuntimeException{
    public RestaurantNotMatchedException() {
    }

    public RestaurantNotMatchedException(String message) {
        super(message);
    }
}
