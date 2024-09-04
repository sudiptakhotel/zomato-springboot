package com.majorproject.zomato.ZomatoApp.dto;

import com.majorproject.zomato.ZomatoApp.entity.enums.OrderStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {


    private Long id;
    private CustomerDTO customer;
    private PartnerDTO partner;
    private List<OrderItemDTO> orderItems;
    private Double amount;
    private RestaurantDTO restaurant;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdTime;//when restaurant accepts orderRequest
    private LocalDateTime deliveredTime; //when its delivered
    private PointDTO restaurantLocation;
    private PointDTO dropOffLocation;

}
