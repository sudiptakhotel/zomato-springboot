package com.majorproject.zomato.ZomatoApp.controller;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.service.CustomerService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customer/order")
@RequiredArgsConstructor
@Secured({"ROLE_CUSTOMER" , "ROLE_ADMIN"})
public class CustomerOrderController {

    private final CustomerService customerService;

    @PostMapping(path = "/placeOrder/{cartId}")
    public ResponseEntity<OrderRequestDTO> placeOrder(@PathVariable Long cartId ,
                                                      @RequestBody PlaceOrderDTO placeOrderDTO) throws MessagingException {

        return new ResponseEntity<>(customerService.placeOrder(cartId , placeOrderDTO)
                , HttpStatus.CREATED);
    }

    @GetMapping(path = "/getOrderItemsInOrderRequest/{orderRequestId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsInOrderRequest(@PathVariable Long orderRequestId) {
        return ResponseEntity.ok(customerService.getOrderItemsByOrderRequestId(orderRequestId));
    }

    @GetMapping(path = "/getOrderById/{orderId}")
    public ResponseEntity<CustomerOrderDTO> getOrderById(@PathVariable Long orderId) {

        return ResponseEntity.ok(customerService.getOrderById(orderId));
    }

    @GetMapping(path = "/getEligiblePromos/{cartId}")
    public ResponseEntity<List<CustomerPromoDTO>> getEligiblePromos(@PathVariable Long cartId) {

        return ResponseEntity.ok(customerService.getEligiblePromos(cartId));
    }

    @GetMapping(path = "/getMyProfile/{customerId}")
    public ResponseEntity<CustomerDTO> gtMyProfile(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getMyProfile(customerId));
    }
    @GetMapping(path = "/getAllMyOrderRequests/{customerId}")
    public ResponseEntity<List<OrderRequestDTO>> getAllMyOrderRequest(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getAllMyOrderRequests(customerId));
    }
    @GetMapping(path = "/getAllMyOrders/{customerId}")
    public ResponseEntity<List<OrderDTO>> getAllMyOrders(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getAllMyOrders(customerId));
    }

    @GetMapping(path = "/getAllMyWalletTransaction/{customerId}")
    public ResponseEntity<List<UserWalletTransactionDTO>> getAllMyWalletTransaction(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getAllMyWalletTransaction(customerId));
    }

    @PostMapping(path = "/ratePartner/{orderId}/{rating}")
    public ResponseEntity<PartnerDTO> ratePartner(@PathVariable Long orderId ,
                                                  @PathVariable Double rating) {
        return ResponseEntity.ok(customerService.ratePartner(orderId , rating));
    }

    @PostMapping(path = "/rateRestaurant/{orderId}/{rating}")
    public ResponseEntity<RestaurantDTO> rateRestaurant(@PathVariable Long orderId ,
                                                  @PathVariable Double rating) {
        return ResponseEntity.ok(customerService.rateRestaurant(orderId , rating));
    }

    @PostMapping(path = "/addBalanceToWallet/{customerId}/{amount}")
    public ResponseEntity<WalletDTO> addBalanceToWallet(@PathVariable Long customerId ,
                                                            @PathVariable Double amount ,
                                                        @RequestParam(defaultValue = "null") String transactionId) {

        return ResponseEntity.ok(customerService.addBalanceToWallet(customerId , amount , transactionId));

    }
}
