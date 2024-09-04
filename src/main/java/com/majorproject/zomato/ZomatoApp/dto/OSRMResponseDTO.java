package com.majorproject.zomato.ZomatoApp.dto;

import lombok.Data;

import java.util.List;

@Data
public class OSRMResponseDTO {
    private List<OSRMRoutesDTO> routes;
}
