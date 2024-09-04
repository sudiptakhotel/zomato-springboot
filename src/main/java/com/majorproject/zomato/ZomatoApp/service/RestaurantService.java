package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantService {

    RestaurantEntity getRestaurantById(Long restaurantId);

    Page<MenuItemDTO> getMenuFromRestaurant(Long restaurantId, Pageable pageable , String sortBy);

    RestaurantDTO getRestaurant(Long restaurantId);

    RestaurantOrderDTO acceptOrderRequest(Long orderRequestId) throws MessagingException;
    RestaurantOrderDTO cancelOrderRequest(Long orderRequestId);
    
    List<OrderDTO> getALlOrders(Long restaurantId);

    RestaurantDTO getProfile(Long restaurantId);

    List<MenuItemDTO> getMenuItems(Long restaurantId);

    List<OrderRequestDTO> getAllOrderRequest(Long restaurantId);

    List<RestaurantWalletTransactionDTO> getAllWalletTransaction(Long restaurantId);
}
