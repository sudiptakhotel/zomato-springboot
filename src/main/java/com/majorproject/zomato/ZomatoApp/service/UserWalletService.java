package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;

public interface UserWalletService {

    UserWalletEntity createWalletForUser(UserEntity user);
    UserWalletEntity addMoneyToUsersWallet(UserEntity user , Double amount , String transactionId , OrderEntity order , TransactionMethod transactionMethod);
    UserWalletEntity deductMoneyFromUserWallet(UserEntity user , Double amount , String transactionId , OrderEntity order , TransactionMethod transactionMethod);


    UserWalletEntity getUserWallet(UserEntity user);
}