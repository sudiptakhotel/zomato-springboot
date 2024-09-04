package com.majorproject.zomato.ZomatoApp.controller;


import com.majorproject.zomato.ZomatoApp.dto.CartDTO;
import com.majorproject.zomato.ZomatoApp.dto.CartItemDTO;
import com.majorproject.zomato.ZomatoApp.dto.ChangeItemQuantityDTO;
import com.majorproject.zomato.ZomatoApp.dto.MenuItemDTO;
import com.majorproject.zomato.ZomatoApp.service.CartRemovalService;
import com.majorproject.zomato.ZomatoApp.service.CartService;
import com.majorproject.zomato.ZomatoApp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/customer/cart")
@RequiredArgsConstructor
@Secured({"ROLE_CUSTOMER" , "ROLE_ADMIN"})
public class CustomerCartController {

    private static final Integer PAGE_SIZE = 5;

    private final CustomerService customerService;
    private final CartService cartService;


    @GetMapping(path = "/getMenuByRestaurantId/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuByRestaurantId(@PathVariable Long restaurantId ,
                                                                   @RequestParam(defaultValue = "0") Integer pageNumber ,
                                                                   @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(pageNumber , PAGE_SIZE);

        return ResponseEntity.ok(customerService.getMenuByRestaurant(restaurantId , pageable , sortBy).getContent());

    }

    @GetMapping(path = "/getCartById/{cartId}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long cartId) {


        return ResponseEntity.ok(cartService.getCartById(cartId));

    }

    @PostMapping(path = "/createCart")
    public ResponseEntity<CartDTO> createCart(@RequestBody CartItemDTO cartItemDTO) {

        return new ResponseEntity<>(cartService.createNewCart(cartItemDTO) , HttpStatus.CREATED);


    }

    @PostMapping("/addItemToCart")
    public ResponseEntity<CartDTO> addItemToCart(@RequestBody CartItemDTO cartItemDTO) {

       return ResponseEntity.ok(cartService.addItemToCart(cartItemDTO));
    }

    @PostMapping(path = "/increaseItemQuantity")
    public ResponseEntity<CartDTO> increaseQuantity(@RequestBody ChangeItemQuantityDTO changeItemQuantityDTO) {

        return ResponseEntity.ok(cartService.increaseQuantity(changeItemQuantityDTO.getOrderItemId()
                , changeItemQuantityDTO.getCartId()
                , changeItemQuantityDTO.getQuantity(),
                changeItemQuantityDTO.getCustomerId()));
    }

    @PostMapping(path = "/decreaseItemQuantity")
    public ResponseEntity<CartDTO> decreaseQuantity(@RequestBody ChangeItemQuantityDTO changeItemQuantityDTO) {

        return ResponseEntity.ok(cartService.decreaseQuantity(changeItemQuantityDTO.getOrderItemId()
                , changeItemQuantityDTO.getCartId()
                , changeItemQuantityDTO.getQuantity(),
                changeItemQuantityDTO.getCustomerId()));
    }
}
