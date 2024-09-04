package com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl;

import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.PaymentEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.repository.PaymentRepository;
import com.majorproject.zomato.ZomatoApp.service.OrderService;
import com.majorproject.zomato.ZomatoApp.service.RestaurantWalletService;
import com.majorproject.zomato.ZomatoApp.service.UserWalletService;
import com.majorproject.zomato.ZomatoApp.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//totalBill - > 100
//partner -> deliveryFee - (deliveryFee*0.2)
//restaurant -> foodAmount
@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final UserWalletService userWalletService;
    private final RestaurantWalletService restaurantWalletService;

    @Override
    public void processPayment(PaymentEntity payment) {

        OrderEntity order = payment.getOrder();
        Double totalBill = payment.getAmount();
        Double foodAmount = order.getFoodAmount();
        Double deliveryFee = order.getDeliveryFee();

        //deduct money from customer's wallet
        userWalletService.deductMoneyFromUserWallet(order.getCustomer().getUser(), totalBill , null , order , TransactionMethod.ORDER);

        //add foodAmount to restaurant's wallet
        restaurantWalletService.addMoneyToRestaurantsWallet(order.getRestaurant() , foodAmount , null , order , TransactionMethod.ORDER);

        //add money to partner's wallet
        userWalletService.addMoneyToUsersWallet(order.getPartner().getUser(), deliveryFee - (deliveryFee * PLATFORM_COMMISSION) , null , order , TransactionMethod.ORDER);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
