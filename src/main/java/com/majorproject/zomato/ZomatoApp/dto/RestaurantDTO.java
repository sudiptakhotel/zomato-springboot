package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    private Long id;
    private String name;
    private String email;
    private String isOpen;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean isVeg;
    private Boolean isRecommendedForChild;
    private PointDTO location;
    private List<MenuItemDTO> menuItems;
}
