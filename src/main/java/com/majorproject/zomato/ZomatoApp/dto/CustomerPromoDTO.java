package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPromoDTO {

    private Long id;
    private String promoName;
    private LocalDate expirationDate;
    private Integer discountPercentage;
    private String description;
}
