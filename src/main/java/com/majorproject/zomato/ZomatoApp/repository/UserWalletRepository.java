package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {

    Optional<UserWalletEntity> findByUser(UserEntity user);
}
