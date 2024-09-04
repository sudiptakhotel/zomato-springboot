package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurant_wallet")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = {"transaction"})
@EqualsAndHashCode(exclude = {"transaction"})
public class RestaurantWalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance = 0.0;

    @OneToOne
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "restaurantWallet")
    private List<RestaurantWalletTransactionEntity> transaction;
}
