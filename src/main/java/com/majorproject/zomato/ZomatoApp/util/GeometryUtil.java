package com.majorproject.zomato.ZomatoApp.util;

import com.majorproject.zomato.ZomatoApp.dto.PointDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {

    public static Point createPoint(PointDTO pointDTO) {

        //SRID of earth's surface is 4326
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel() , 4326);

        //create the coordinate
        Coordinate coordinate = new Coordinate(pointDTO.getCoordinate()[0],
                pointDTO.getCoordinate()[1]);

        //create a point
        return geometryFactory.createPoint(coordinate);

    }
}
