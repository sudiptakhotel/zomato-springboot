package com.majorproject.zomato.ZomatoApp.strategy.strategyManager;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.strategy.DeliveryFeeCalculationStrategy;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.DeliveryFeeCalculationDefault;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.DeliveryFeeCalculationOrderAmount;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.DeliveryFeeCalculationSurge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class DeliveryFeeStrategyManager {

    private final DeliveryFeeCalculationDefault deliveryFeeCalculationDefault;
    private final DeliveryFeeCalculationSurge deliveryFeeCalculationSurge;
    private final DeliveryFeeCalculationOrderAmount deliveryFeeCalculationOrderAmount;

    public DeliveryFeeCalculationStrategy getDeliveryFeeCalculationStrategy(CartEntity cart) {

        LocalTime lunchSurgeStartTime = LocalTime.of(12 , 0);
        LocalTime lunchSurgeEndTime = LocalTime.of(14 , 0);
        LocalTime dinnerSurgeStartTime = LocalTime.of(20 , 0);
        LocalTime dinnerSurgeEndTime = LocalTime.of(23 , 0);
        LocalTime currentTime = LocalTime.now();

        if(cart.getFoodAmount() >= 1500)
            return deliveryFeeCalculationOrderAmount;

        else if(currentTime.isAfter(lunchSurgeStartTime) && currentTime.isBefore(lunchSurgeEndTime))
            return deliveryFeeCalculationSurge;

        else if(currentTime.isAfter(dinnerSurgeStartTime) && currentTime.isBefore(dinnerSurgeEndTime))
            return deliveryFeeCalculationSurge;

        else
            return deliveryFeeCalculationDefault;
    }
}
