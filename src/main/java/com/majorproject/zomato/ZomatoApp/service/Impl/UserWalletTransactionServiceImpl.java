package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletTransactionEntity;
import com.majorproject.zomato.ZomatoApp.repository.UserWalletTransactionRepository;
import com.majorproject.zomato.ZomatoApp.service.UserWalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWalletTransactionServiceImpl implements UserWalletTransactionService {

    private final UserWalletTransactionRepository userWalletTransactionRepository;

    @Override
    public List<UserWalletTransactionEntity> getAllUserWalletTransaction(UserWalletEntity userWallet) {
        return userWalletTransactionRepository.findByWallet(userWallet)
                .orElse(null);
    }
}
