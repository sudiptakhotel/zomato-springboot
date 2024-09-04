package com.majorproject.zomato.ZomatoApp.dto;

import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDTO {


    private Long id;
    private Double amount;
    private WalletDTO wallet;
    private TransactionType transactionType;
    private TransactionMethod transactionMethod;
    private LocalDateTime timestamp;
    private OrderDTO order;
    private String transactionId;
}
