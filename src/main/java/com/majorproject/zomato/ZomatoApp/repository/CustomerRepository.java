package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity , Long> {

    Optional<CustomerEntity> findByUser(UserEntity user);
}
