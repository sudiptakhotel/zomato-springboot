package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderEntity createOrder(OrderRequestEntity orderRequest);

    OrderEntity getOrderById(Long orderId);

    OrderEntity saveOrder(OrderEntity order);

    String getRestaurantOtp(Long orderId);
    String getOtp(Long orderId);

    String generateRandomRestaurantOTP();
    String generateRandomOTP();

    OrderEntity updateOrderStatus(OrderEntity order , OrderStatus orderStatus);

    List<OrderEntity> getOrdersByCustomer(CustomerEntity customer);

    List<OrderEntity> getOrdersByPartner(PartnerEntity partner);

    List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurant);
}
