package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOtpDTO {

    private Long orderId;
    private String otp;
}
