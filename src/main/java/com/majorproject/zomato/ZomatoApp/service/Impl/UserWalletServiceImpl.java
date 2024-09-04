package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletTransactionEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionType;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.UserWalletRepository;
import com.majorproject.zomato.ZomatoApp.repository.UserWalletTransactionRepository;
import com.majorproject.zomato.ZomatoApp.service.UserWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWalletServiceImpl implements UserWalletService {

    private final UserWalletRepository userWalletRepository;
    private final UserWalletTransactionRepository walletTransactionRepository;


    @Override
    public UserWalletEntity createWalletForUser(UserEntity user) {

        UserWalletEntity wallet = new UserWalletEntity();
        wallet.setUser(user);

        userWalletRepository.save(wallet);
        return wallet;
    }

    @Override
    public UserWalletEntity addMoneyToUsersWallet(UserEntity user, Double amount, String transactionId, OrderEntity order, TransactionMethod transactionMethod) {

        UserWalletEntity wallet = userWalletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("wallet not found for " +
                        "user , with userId : "+user.getId()));

        Double currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance + amount);


        //create a UserWalletTransactionEntity
        UserWalletTransactionEntity walletTransaction = new UserWalletTransactionEntity();
        walletTransaction.setWallet(wallet);
        walletTransaction.setTransactionId(transactionId);
        walletTransaction.setAmount(amount);
        walletTransaction.setOrder(order);
        walletTransaction.setTransactionMethod(transactionMethod);
        walletTransaction.setTransactionType(TransactionType.CREDIT);

        walletTransactionRepository.save(walletTransaction);

        wallet.getTransaction().add(walletTransaction);

        return userWalletRepository.save(wallet);

    }

    @Override
    public UserWalletEntity deductMoneyFromUserWallet(UserEntity user, Double amount, String transactionId, OrderEntity order, TransactionMethod transactionMethod) {

        UserWalletEntity wallet = userWalletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("wallet not found for " +
                        "user , with userId : "+user.getId()));

        Double currentBalance = wallet.getBalance();
        wallet.setBalance(currentBalance - amount);


        //create a UserWalletTransactionEntity
        UserWalletTransactionEntity walletTransaction = new UserWalletTransactionEntity();
        walletTransaction.setWallet(wallet);
        walletTransaction.setTransactionId(transactionId);
        walletTransaction.setAmount(amount);
        walletTransaction.setOrder(order);
        walletTransaction.setTransactionMethod(transactionMethod);
        walletTransaction.setTransactionType(TransactionType.DEBIT);

        walletTransactionRepository.save(walletTransaction);

        wallet.getTransaction().add(walletTransaction);

        return userWalletRepository.save(wallet);

    }

    @Override
    public UserWalletEntity getUserWallet(UserEntity user) {

        return userWalletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user , " +
                        "with userId : "+user.getId()));
    }

}
