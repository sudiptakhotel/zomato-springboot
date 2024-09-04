package com.majorproject.zomato.ZomatoApp.repository;

import com.majorproject.zomato.ZomatoApp.entity.PromoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PromoRepository extends JpaRepository<PromoEntity , Long> {

    Optional<PromoEntity> findByPromoName(String promoName);

    List<PromoEntity> findByThresholdAmountLessThanEqualAndExpirationDateAfter(Double totalAmount, LocalDate currentDate);

    Optional<List<PromoEntity>> findByExpirationDateBefore(LocalDate currentDate);

    ;
}
