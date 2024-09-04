package com.majorproject.zomato.ZomatoApp.dto;

import lombok.Data;

@Data
public class ChangeItemQuantityDTO {

    private Long customerId;
    private Long orderItemId;
    private Long cartId;
    private Integer quantity;
}
