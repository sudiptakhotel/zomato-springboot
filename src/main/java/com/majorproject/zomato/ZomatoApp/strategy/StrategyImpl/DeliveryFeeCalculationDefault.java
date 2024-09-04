package com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.service.DistanceService;
import com.majorproject.zomato.ZomatoApp.strategy.DeliveryFeeCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryFeeCalculationDefault implements DeliveryFeeCalculationStrategy {

    private final DistanceService distanceService;



    @Override
    public Double calculateDeliveryFee(CartEntity cart) {

        Double distance = distanceService.calculateDistance(cart.getRestaurant().getRestaurantLocation()
                , cart.getDropOffLocation());

        if(distance > 15.00) {
            return distance * DELIVERY_FARE_MULTIPLIER * 0.75;
        }

        return distance * DELIVERY_FARE_MULTIPLIER;
    }
}
