package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity , Long> {

   Optional<CartEntity> findByCustomer(CustomerEntity customer);
}
