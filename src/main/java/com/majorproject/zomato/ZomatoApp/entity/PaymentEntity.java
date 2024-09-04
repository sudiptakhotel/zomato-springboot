package com.majorproject.zomato.ZomatoApp.entity;

import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private Double amount;

    @OneToOne
    private OrderEntity order;

    private LocalDateTime paymentTime;
}
