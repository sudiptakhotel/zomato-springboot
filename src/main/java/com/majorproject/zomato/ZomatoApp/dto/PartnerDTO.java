package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {


    private Long id;
    private UserDTO user;
    private Boolean available;
    private PointDTO currentLocation;
}
