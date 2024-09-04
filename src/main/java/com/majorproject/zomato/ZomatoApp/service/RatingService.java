package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.CustomerDTO;
import com.majorproject.zomato.ZomatoApp.dto.PartnerDTO;
import com.majorproject.zomato.ZomatoApp.dto.RestaurantDTO;
import com.majorproject.zomato.ZomatoApp.entity.*;

public interface RatingService {

    PartnerDTO ratePartner(OrderEntity order , Double rating);
    CustomerDTO rateCustomer(OrderEntity order , Double rating);
    RestaurantDTO rateRestaurant(OrderEntity order , Double rating);

    void createRating(OrderEntity order , PartnerEntity partner ,
                      CustomerEntity customer , RestaurantEntity restaurant);

}
