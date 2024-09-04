package com.majorproject.zomato.ZomatoApp.config;

import com.majorproject.zomato.ZomatoApp.dto.PointDTO;
import com.majorproject.zomato.ZomatoApp.util.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        //conversion of PointDTO --> Point
        modelMapper.typeMap(PointDTO.class , Point.class).setConverter(mappingContext -> {

            //extract the source
            PointDTO pointDTO = mappingContext.getSource();
            return GeometryUtil.createPoint(pointDTO);
        });

        //conversion of Point to PointDTO

        modelMapper.typeMap(Point.class , PointDTO.class).setConverter(mappingContext -> {

            Point point = mappingContext.getSource();
            double[] coordinate = new double[2];

            coordinate[0] = point.getX();
            coordinate[1] = point.getY();

            return new PointDTO(coordinate);
        });

        return modelMapper;
    }
}
