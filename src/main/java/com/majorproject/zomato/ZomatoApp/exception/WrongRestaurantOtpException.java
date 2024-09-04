package com.majorproject.zomato.ZomatoApp.exception;

public class WrongRestaurantOtpException extends RuntimeException{
    public WrongRestaurantOtpException() {
    }

    public WrongRestaurantOtpException(String message) {
        super(message);
    }
}
