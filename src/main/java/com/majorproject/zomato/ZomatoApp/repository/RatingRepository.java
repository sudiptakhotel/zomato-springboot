package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<RatingEntity , Long> {

    List<RatingEntity> findByCustomer(CustomerEntity customer);
    List<RatingEntity> findByPartner(PartnerEntity partner);
    List<RatingEntity> findByRestaurant(RestaurantEntity restaurant);

    Optional<RatingEntity> findByOrder(OrderEntity order);
}
