package com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.PaymentEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.repository.PaymentRepository;
import com.majorproject.zomato.ZomatoApp.service.RestaurantWalletService;
import com.majorproject.zomato.ZomatoApp.service.UserWalletService;
import com.majorproject.zomato.ZomatoApp.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

//let say the orderAmount is 100 rs
//foodAmount  = 80
//deliveryFee = 20
//zomato 20% of deliveryFee
@Service
@RequiredArgsConstructor
public class CODPaymentStrategy implements PaymentStrategy {

    private final RestaurantWalletService restaurantWalletService;
    private final UserWalletService userWalletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(PaymentEntity payment) {

        OrderEntity order = payment.getOrder();
        Double totalBill = payment.getAmount();
        Double foodAmount = order.getFoodAmount();
        Double deliveryFee = order.getDeliveryFee();

        //split the amount

        //deduct this partnerFee to partner's wallet
        Double partnerFee = deliveryFee - (deliveryFee * PLATFORM_COMMISSION);
        userWalletService.deductMoneyFromUserWallet(order.getPartner().getUser(), totalBill - partnerFee ,null , order , TransactionMethod.ORDER);

        //pay full foodAmount to restaurant's wallet
        restaurantWalletService.addMoneyToRestaurantsWallet(order.getRestaurant() , foodAmount , null , order , TransactionMethod.ORDER);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        payment.setPaymentTime(LocalDateTime.now());
        paymentRepository.save(payment);
    }
}
