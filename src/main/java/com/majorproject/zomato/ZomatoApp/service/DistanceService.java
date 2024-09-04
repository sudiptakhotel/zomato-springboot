package com.majorproject.zomato.ZomatoApp.service;


import org.locationtech.jts.geom.Point;

public interface DistanceService {

    Double calculateDistance(Point source , Point destination);
}
