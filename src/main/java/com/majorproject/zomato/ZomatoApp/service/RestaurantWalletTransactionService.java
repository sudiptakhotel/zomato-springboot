package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletTransactionEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletTransactionEntity;

import java.util.List;

public interface RestaurantWalletTransactionService {
    List<RestaurantWalletTransactionEntity> getAllWalletTransaction(RestaurantWalletEntity restaurantWallet);
}
