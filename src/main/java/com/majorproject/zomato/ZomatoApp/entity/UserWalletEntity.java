package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_wallet")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"transaction"})
@EqualsAndHashCode(exclude = {"transaction"})
public class UserWalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double balance = 0.0;

    @OneToOne
    private UserEntity user;

    @OneToMany(mappedBy = "wallet")
    private List<UserWalletTransactionEntity> transaction;

}
