package com.majorproject.zomato.ZomatoApp.exception;

public class WrongCustomerOtpException extends RuntimeException{
    public WrongCustomerOtpException() {
    }

    public WrongCustomerOtpException(String message) {
        super(message);
    }
}
