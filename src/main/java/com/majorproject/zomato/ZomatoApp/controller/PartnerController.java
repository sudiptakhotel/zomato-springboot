package com.majorproject.zomato.ZomatoApp.controller;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/partner")
@RequiredArgsConstructor
@Secured({"ROLE_PARTNER" , "ROLE_ADMIN"})
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping(path = "/getMyProfile/{partnerId}")
    public ResponseEntity<PartnerDTO> getMyProfile(@PathVariable Long partnerId) {
        return ResponseEntity.ok(partnerService.getMyProfile(partnerId));
    }

    @GetMapping(path = "/getAllMyOrders/{partnerId}")
    public ResponseEntity<List<OrderDTO>> getAllMyOrders(@PathVariable Long partnerId) {
        return ResponseEntity.ok(partnerService.getAllOrders(partnerId));
    }

    @GetMapping(path = "/getAllMyWalletTransaction/{partnerId}")
    public ResponseEntity<List<UserWalletTransactionDTO>> getAllMyWalletTransaction(@PathVariable Long partnerId) {
        return ResponseEntity.ok(partnerService.getAllMyWalletTransaction(partnerId));
    }

    @PostMapping(path = "/acceptOrder/{orderId}")
    public ResponseEntity<PartnerOrderDTO> acceptOrder(@PathVariable Long orderId) {

        return ResponseEntity.ok(partnerService.acceptOrder(orderId));
    }

    @PostMapping(path = "/pickUpOrderFromRestaurant")
    public ResponseEntity<PartnerOrderDTO> pickUpOrderFromRestaurant(@RequestBody OrderOtpDTO orderOtpDTO) {

        return ResponseEntity.ok(partnerService.pickupOrderFromRestaurant(orderOtpDTO.getOrderId(), orderOtpDTO.getOtp()));
    }

    @PostMapping(path = "/startOrderRide/{orderId}")
    public ResponseEntity<PartnerOrderDTO> startOrderRide(@PathVariable Long orderId) {

        return ResponseEntity.ok(partnerService.startOrderRide(orderId));
    }

    @PostMapping(path = "/cancelOrder/{orderId}")
    public ResponseEntity<PartnerOrderDTO> cancelOrder(@PathVariable Long orderId) {

        return ResponseEntity.ok(partnerService.cancelOrder(orderId));
    }

    @PostMapping(path = "/endOrderRide/{orderId}")
    public ResponseEntity<PartnerOrderDTO> endOrderRide(@RequestBody OrderOtpDTO orderOtpDTO) {
        return ResponseEntity.ok(partnerService.endOrderRide(orderOtpDTO.getOrderId() , orderOtpDTO.getOtp()));
    }

    @PostMapping(path = "/rateCustomer/{orderId}/{rating}")
    public ResponseEntity<CustomerDTO> rateCustomer(@PathVariable Long orderId ,
                                                    @PathVariable Double rating) {
        return ResponseEntity.ok(partnerService.rateCustomer(orderId , rating));
    }

    @PostMapping(path = "/addBalanceToWallet/{partnerId}/{amount}")
    public ResponseEntity<WalletDTO> addBalanceToWallet(@PathVariable Long partnerId ,
                                                        @PathVariable Double amount ,
                                                        @RequestParam(defaultValue = "null") String transactionId) {

        return ResponseEntity.ok(partnerService.addBalanceToWallet(partnerId , amount , transactionId));

    }
}
