package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "promo" ,
indexes = {
        @Index(name = "idx_promo_expirationDate" , columnList = "expiration_date")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PromoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String promoName;

    private LocalDate expirationDate;
    private Integer discountPercentage;

    private Double thresholdAmount; //above this amount promo will be applied
    private String description;
}
