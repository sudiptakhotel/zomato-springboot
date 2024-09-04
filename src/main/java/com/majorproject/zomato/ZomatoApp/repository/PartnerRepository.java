package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import org.hibernate.annotations.QueryCacheLayout;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity , Long> {

    @Query(value = "SELECT p.* , ST_Distance(p.current_location , :restaurantLocation) AS distance " +
            "FROM partner p " +
            "where p.available = true AND ST_DWithin(p.current_location , :restaurantLocation , 5000) " +
            "ORDER BY distance " +
            "LIMIT 10" , nativeQuery = true)
    List<PartnerEntity> findTenNearestPartner(Point restaurantLocation);

    Optional<PartnerEntity> findByUser(UserEntity user);

    @Query(value = "SELECT p.* " +
            "FROM partner p " +
            "WHERE p.available = true AND ST_DWithin(p.current_location , :restaurantLocation , 10000) " +
            "ORDER BY rating " +
            "LIMIT 10" , nativeQuery = true)
    List<PartnerEntity> findTenNearestTopRatedPartner(Point restaurantLocation);
}
