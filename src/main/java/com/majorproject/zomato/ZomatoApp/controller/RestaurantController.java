package com.majorproject.zomato.ZomatoApp.controller;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.service.RestaurantService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;


    @GetMapping(path = "/getAllOrderRequest/{restaurantId}")
    public ResponseEntity<List<OrderRequestDTO>> getAllOrderRequest(@PathVariable Long restaurantId) {

        return ResponseEntity.ok(restaurantService.getAllOrderRequest(restaurantId));
    }

    @GetMapping(path = "/getAllWalletTransaction/{restaurantId}")
    public ResponseEntity<List<RestaurantWalletTransactionDTO>> getAllWalletTransaction(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getAllWalletTransaction(restaurantId));
    }

    @GetMapping(path = "/getAllOrders/{restaurantId}")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getALlOrders(restaurantId));
    }

    @GetMapping(path = "/getProfile/{restaurantId}")
    public ResponseEntity<RestaurantDTO> getProfile(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getProfile(restaurantId));
    }

    @GetMapping(path = "/getMenuItems/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByRestaurantId(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantService.getMenuItems(restaurantId));
    }

    @PostMapping(path = "/acceptOrderRequest/{orderRequestId}")
    public ResponseEntity<RestaurantOrderDTO> acceptOrderRequest(@PathVariable Long orderRequestId) throws MessagingException {
        return ResponseEntity.ok(restaurantService.acceptOrderRequest(orderRequestId));
    }

}
