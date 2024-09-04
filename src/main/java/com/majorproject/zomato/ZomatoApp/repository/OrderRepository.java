package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity , Long> {

    Optional<List<OrderEntity>> findByCustomer(CustomerEntity customer);

    Optional<List<OrderEntity>> findByPartner(PartnerEntity partner);

    Optional<List<OrderEntity>> findByRestaurant(RestaurantEntity restaurant);
}
