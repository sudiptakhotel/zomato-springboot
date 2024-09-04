package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.MenuItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity , Long> {

    Optional<List<MenuItemEntity>> findByRestaurant(RestaurantEntity restaurant);
}
