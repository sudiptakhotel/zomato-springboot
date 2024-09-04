package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.OrderRequestDTO;
import com.majorproject.zomato.ZomatoApp.dto.PlaceOrderDTO;
import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;

public interface OrderRequestService {

    //OrderRequestDTO createOrderRequest(Long cartId , PlaceOrderDTO placeOrderDTO);

    OrderRequestEntity getOrderRequest(Long orderRequest);

    OrderRequestEntity saveOrderRequest(OrderRequestEntity orderRequest);
}
