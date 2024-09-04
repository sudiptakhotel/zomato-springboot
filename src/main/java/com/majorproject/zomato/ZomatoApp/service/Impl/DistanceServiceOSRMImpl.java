package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.OSRMResponseDTO;
import com.majorproject.zomato.ZomatoApp.exception.OSRMException;
import com.majorproject.zomato.ZomatoApp.service.DistanceService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class DistanceServiceOSRMImpl implements DistanceService {

    private static final String BASE_URL_OSRM_SERVER = "http://router.project-osrm.org/route/v1/driving/";

    private final RestClient restClient;

    @Override
    public Double calculateDistance(Point source, Point destination) {

        try {
            //fetching the distance from OSRM server

            String uri = source.getX()+","+source.getY()+";"+destination.getX()+","+destination.getY();

            OSRMResponseDTO response = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseDTO.class);

            return response.getRoutes().get(0).getDistance() / 1000;
        } catch (Exception exception) {
            throw new OSRMException("Not able to fetch distance from " +
                    "OSRM server , "+exception.getMessage());
        }

    }
}
