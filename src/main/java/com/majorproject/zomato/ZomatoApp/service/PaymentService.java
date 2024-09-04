package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.PaymentEntity;

public interface PaymentService {

    void processPayment(OrderEntity order);
    PaymentEntity createPayment(OrderEntity order);
}
