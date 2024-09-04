package com.majorproject.zomato.ZomatoApp.dto;

import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDTO {

    private Long cartId;
    private PaymentMethod paymentMethod;
    private Long promoId;
}
