package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long restaurantId;
    private Long menuItemId;
    private Integer quantity;
    private PointDTO dropOffLocation;

}
