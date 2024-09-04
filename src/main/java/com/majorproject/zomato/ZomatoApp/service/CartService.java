package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.CartDTO;
import com.majorproject.zomato.ZomatoApp.dto.CartItemDTO;
import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.ItemAction;

public interface CartService {


    CartDTO addItemToCart(CartItemDTO cartItemDTO);
    CartEntity removeItemFromCart(Long orderItemId , CartEntity cart);
    CartDTO getTotalCartAmount(Long cartId);
    CartEntity getCartByCustomer(CustomerEntity customer);

    CartDTO getCartById(Long cartId);

    CartEntity createCart(CustomerEntity customer, RestaurantEntity restaurant , CartItemDTO cartItemDTO);

    CartEntity updateAmount(CartEntity cart , Double amount , ItemAction itemAction);

    CartDTO increaseQuantity(Long orderItemId, Long cartId , Integer quantity , Long customerId);

    CartDTO decreaseQuantity(Long orderItemId, Long cartId, Integer quantity , Long customerId);


    CartDTO createNewCart(CartItemDTO cartItemDTO);
}
