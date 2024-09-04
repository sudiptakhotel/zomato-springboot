package com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.service.DistanceService;
import com.majorproject.zomato.ZomatoApp.strategy.DeliveryFeeCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationOrderAmount implements DeliveryFeeCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public Double calculateDeliveryFee(CartEntity cart) {

        //if the order amount is greater than 1500 , we do not charge deliveryFee
        return 0.0;

    }
}
