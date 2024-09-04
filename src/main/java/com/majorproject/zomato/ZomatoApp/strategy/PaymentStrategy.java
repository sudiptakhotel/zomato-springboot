package com.majorproject.zomato.ZomatoApp.strategy;

import com.majorproject.zomato.ZomatoApp.entity.PaymentEntity;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.2;
    void processPayment(PaymentEntity payment);
}
