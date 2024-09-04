package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantWalletRepository extends JpaRepository<RestaurantWalletEntity , Long> {


    Optional<RestaurantWalletEntity> findByRestaurant(RestaurantEntity restaurant);
}
