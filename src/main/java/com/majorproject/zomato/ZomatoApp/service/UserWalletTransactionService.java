package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletTransactionEntity;

import java.util.List;

public interface UserWalletTransactionService {
    List<UserWalletTransactionEntity> getAllUserWalletTransaction(UserWalletEntity userWallet);
}
