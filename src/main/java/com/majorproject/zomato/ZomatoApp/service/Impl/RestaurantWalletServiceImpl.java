package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantWalletTransactionEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionType;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.RestaurantWalletRepository;
import com.majorproject.zomato.ZomatoApp.repository.RestaurantWalletTransactionRepository;
import com.majorproject.zomato.ZomatoApp.service.RestaurantWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantWalletServiceImpl implements RestaurantWalletService {

    private final RestaurantWalletRepository restaurantWalletRepository;
    private final RestaurantWalletTransactionRepository restaurantWalletTransactionRepository;

    @Override
    public RestaurantWalletEntity createWalletForRestaurant(RestaurantEntity restaurant) {

        RestaurantWalletEntity wallet = new RestaurantWalletEntity();
        wallet.setRestaurant(restaurant);
        return restaurantWalletRepository.save(wallet);
    }

    @Override
    public RestaurantWalletEntity addMoneyToRestaurantsWallet(RestaurantEntity restaurant, Double amount, String transactionId, OrderEntity order, TransactionMethod transactionMethod) {

        RestaurantWalletEntity wallet = restaurantWalletRepository.findByRestaurant(restaurant)
                .orElseThrow(() -> new ResourceNotFoundException("wallet not found for " +
                        "restaurant , with restaurantId : "+restaurant.getId()));

        Double currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance + amount);


        //create a UserWalletTransactionEntity
        RestaurantWalletTransactionEntity restaurantWalletTransaction = new RestaurantWalletTransactionEntity();
        restaurantWalletTransaction.setRestaurantWallet(wallet);
        restaurantWalletTransaction.setTransactionId(transactionId);
        restaurantWalletTransaction.setOrder(order);
        restaurantWalletTransaction.setAmount(amount);
        restaurantWalletTransaction.setTransactionMethod(transactionMethod);
        restaurantWalletTransaction.setTransactionType(TransactionType.CREDIT);

        //save the transaction
        restaurantWalletTransactionRepository.save(restaurantWalletTransaction);

        //add this transaction to wallet
        wallet.getTransaction().add(restaurantWalletTransaction);

        return restaurantWalletRepository.save(wallet);
    }

    @Override
    public RestaurantWalletEntity deductMoneyFromRestaurantWallet(RestaurantEntity restaurant, Double amount, String transactionId, OrderEntity order, TransactionMethod transactionMethod) {

        RestaurantWalletEntity wallet = restaurantWalletRepository.findByRestaurant(restaurant)
                .orElseThrow(() -> new ResourceNotFoundException("wallet not found for " +
                        "restaurant , with restaurantId : "+restaurant.getId()));

        Double currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance - amount);


        //create a UserWalletTransactionEntity
        RestaurantWalletTransactionEntity restaurantWalletTransaction = new RestaurantWalletTransactionEntity();
        restaurantWalletTransaction.setRestaurantWallet(wallet);
        restaurantWalletTransaction.setTransactionId(transactionId);
        restaurantWalletTransaction.setOrder(order);
        restaurantWalletTransaction.setAmount(amount);
        restaurantWalletTransaction.setTransactionMethod(transactionMethod);
        restaurantWalletTransaction.setTransactionType(TransactionType.DEBIT);

        //save the transaction
        restaurantWalletTransactionRepository.save(restaurantWalletTransaction);

        //add this transaction to wallet
        wallet.getTransaction().add(restaurantWalletTransaction);

        return restaurantWalletRepository.save(wallet);

    }

    @Override
    public RestaurantWalletEntity getRestaurantWallet(RestaurantEntity restaurant) {
        return restaurantWalletRepository.findByRestaurant(restaurant)
                .orElseThrow(() -> new ResourceNotFoundException("No wallet found for " +
                        "restaurant , with restaurantId : "+restaurant.getId()));
    }
}
