package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantWalletTransactionRepository extends JpaRepository<RestaurantWalletTransactionEntity , Long> {

    Optional<List<RestaurantWalletTransactionEntity>> findByRestaurantWallet(RestaurantWalletEntity restaurantWallet);
}
