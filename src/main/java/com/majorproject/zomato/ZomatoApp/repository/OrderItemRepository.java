package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity , Long> {

    Optional<OrderItemEntity> findByItemName(String name);

    Optional<List<OrderItemEntity>> findByCart(CartEntity cart);

    Optional<List<OrderItemEntity>> findByOrderRequest(OrderRequestEntity orderRequest);
}
