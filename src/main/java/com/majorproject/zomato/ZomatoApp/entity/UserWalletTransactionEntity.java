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
@Table(name = "user_wallet_transaction" ,
indexes = {
        @Index(name = "idx_user_wallet_transaction_wallet" , columnList = "wallet_id") ,
        @Index(name = "idx_user_wallet_transaction_order" , columnList = "order_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserWalletTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne
    private UserWalletEntity wallet;

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
