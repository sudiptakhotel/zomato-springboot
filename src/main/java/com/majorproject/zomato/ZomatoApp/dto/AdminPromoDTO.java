package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPromoDTO {

    private Long id;
    private String promoName;
    private LocalDate expirationDate;
    private Integer discountPercentage;
    private Double thresholdAmount; //above this amount promo will be applied
    private String description;
}
