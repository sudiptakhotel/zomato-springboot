package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserWalletTransactionRepository extends JpaRepository<UserWalletTransactionEntity, Long> {

    Optional<List<UserWalletTransactionEntity>> findByWallet(UserWalletEntity userWallet);
}
