package com.majorproject.zomato.ZomatoApp.strategy;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;

public interface DeliveryFeeCalculationStrategy {

    final double DELIVERY_FARE_MULTIPLIER = 8.0;
    Double calculateDeliveryFee(CartEntity cart);
}
