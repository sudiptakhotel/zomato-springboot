package com.majorproject.zomato.ZomatoApp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointDTO {
    private double[] coordinate;
    private String type = "point";

    public PointDTO(double[] coordinate) {

        this.coordinate = coordinate;
    }
}
