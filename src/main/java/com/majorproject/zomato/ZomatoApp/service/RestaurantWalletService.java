package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;

public interface RestaurantWalletService {

    RestaurantWalletEntity createWalletForRestaurant(RestaurantEntity restaurant);
    RestaurantWalletEntity addMoneyToRestaurantsWallet(RestaurantEntity restaurant , Double amount , String transactionId , OrderEntity order , TransactionMethod transactionMethod);
    RestaurantWalletEntity deductMoneyFromRestaurantWallet(RestaurantEntity restaurant , Double amount , String transactionId , OrderEntity order , TransactionMethod transactionMethod);

    RestaurantWalletEntity getRestaurantWallet(RestaurantEntity restaurant);
}
