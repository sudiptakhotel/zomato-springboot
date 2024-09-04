package com.majorproject.zomato.ZomatoApp.dto;

import com.majorproject.zomato.ZomatoApp.entity.enums.OrderRequestStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private Long id;
    private Long cartId;
    private List<OrderItemDTO> orderItems;

    private Double amount;
    private Double foodAmount;
    private Double deliveryFee;
    private String restaurantName;
    private OrderRequestStatus status;
    private PaymentMethod paymentMethod;

    private CustomerDTO customer;
    private PointDTO dropOffLocation;

    private Boolean promoApplied;
    private String discountPercentage;

}
