package com.majorproject.zomato.ZomatoApp.entity;

import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_wallet_transaction" ,
indexes = {
        @Index(name = "idx_restaurant_wallet_transaction_wallet" , columnList = "restaurant_wallet_id") ,
        @Index(name = "idX_restaurant_wallet_transaction_order" , columnList = "order_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestaurantWalletTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne
    private RestaurantWalletEntity restaurantWallet;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    private OrderEntity order;

    private String transactionId; //related to bank transaction
}
