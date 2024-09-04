package com.majorproject.zomato.ZomatoApp.service;


import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.OrderEntity;
import com.majorproject.zomato.ZomatoApp.entity.PartnerEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;

import java.util.List;

public interface PartnerService {

    PartnerOrderDTO acceptOrder(Long orderId);
    PartnerOrderDTO cancelOrder(Long orderId);
    PartnerOrderDTO pickupOrderFromRestaurant(Long orderId , String restaurantOTP);
    PartnerOrderDTO startOrderRide(Long orderId);
    PartnerOrderDTO endOrderRide(Long orderId , String otp);

    PartnerEntity getPartnerByUserId(Long userId);

    PartnerEntity createPartner(UserEntity savedUser, PointDTO pointDTO);

    CustomerDTO rateCustomer(Long orderId , Double rating);

    PartnerDTO getMyProfile(Long partnerId);

    List<OrderDTO> getAllOrders(Long partnerId);

    List<UserWalletTransactionDTO> getAllMyWalletTransaction(Long partnerId);

    WalletDTO addBalanceToWallet(Long partnerId, Double amount, String transactionId);
}

