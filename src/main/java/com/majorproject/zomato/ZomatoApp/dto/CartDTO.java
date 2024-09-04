package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {


    private Long id;
    private CustomerDTO customer;
    private List<OrderItemDTO> orderItems;
    private Double deliverFee;
    private Double totalAmount;
    private Long restaurantId;
    private PointDTO dropOffLocation;

}
