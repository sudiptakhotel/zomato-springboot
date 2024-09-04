package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.PaymentEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentStatus;
import com.majorproject.zomato.ZomatoApp.repository.PaymentRepository;
import com.majorproject.zomato.ZomatoApp.service.PaymentService;
import com.majorproject.zomato.ZomatoApp.strategy.strategyManager.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(OrderEntity order) {

        PaymentEntity payment = paymentRepository.findByOrder(order);
        paymentStrategyManager.getPaymentStrategy(order.getPaymentMethod())
                .processPayment(payment);
    }

    @Override
    public PaymentEntity createPayment(OrderEntity order) {

        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentMethod(order.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(order.getAmount());
        payment.setOrder(order);

        return paymentRepository.save(payment);
    }
}
