package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletTransactionEntity;
import com.majorproject.zomato.ZomatoApp.repository.RestaurantWalletTransactionRepository;
import com.majorproject.zomato.ZomatoApp.service.RestaurantWalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantWalletTransactionServiceImpl implements RestaurantWalletTransactionService {

    private final RestaurantWalletTransactionRepository restaurantWalletTransactionRepository;

    @Override
    public List<RestaurantWalletTransactionEntity> getAllWalletTransaction(RestaurantWalletEntity restaurantWallet) {

        return restaurantWalletTransactionRepository.findByRestaurantWallet(restaurantWallet)
                .orElse(null);
    }
}
