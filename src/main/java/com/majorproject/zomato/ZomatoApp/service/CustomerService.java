package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.entity.UserEntity;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {

    CustomerEntity getCustomer();

    Page<MenuItemDTO> getMenuByRestaurant(Long restaurantId, Pageable pageable , String sortBy);

    CustomerEntity getCustomerById(Long customerId);

    OrderRequestDTO placeOrder(Long cartId , PlaceOrderDTO placeOrderDTO) throws MessagingException;

    CustomerOrderDTO getOrderById(Long orderId);

    CustomerEntity createCustomer(UserEntity savedUser);

    List<CustomerPromoDTO> getEligiblePromos(Long cartId);

    PartnerDTO ratePartner(Long orderId , Double rating);
    RestaurantDTO rateRestaurant(Long orderId , Double rating);

    CustomerDTO getMyProfile(Long customerId);

    List<OrderDTO> getAllMyOrders(Long customerId);
    List<OrderItemDTO> getOrderItemsByOrderRequestId(Long orderRequestId);

    List<OrderRequestDTO> getAllMyOrderRequests(Long customerId);

    List<UserWalletTransactionDTO> getAllMyWalletTransaction(Long customerId);

    WalletDTO addBalanceToWallet(Long customerId, Double amount , String transactionId);
}
