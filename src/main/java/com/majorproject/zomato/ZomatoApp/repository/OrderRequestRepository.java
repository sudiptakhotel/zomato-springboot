package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRequestRepository extends JpaRepository<OrderRequestEntity , Long> {

    Optional<List<OrderRequestEntity>> findByRestaurant(RestaurantEntity restaurant);

    Optional<List<OrderRequestEntity>> findByCustomer(CustomerEntity customer);
}
