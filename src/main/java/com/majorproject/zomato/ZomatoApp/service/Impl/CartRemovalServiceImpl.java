package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.exception.CustomerNotMatchedException;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.CartRepository;
import com.majorproject.zomato.ZomatoApp.service.CartRemovalService;
import com.majorproject.zomato.ZomatoApp.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartRemovalServiceImpl implements CartRemovalService {

    private final CartRepository cartRepository;
    private final OrderItemService orderItemService;

    @Override
    public void removeCart(Long cartId , CustomerEntity customer) {

        //TODO : get customer from security context holder

        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with " +
                        "id "+cartId));

        //check if the same customer calling the API
        if(!customer.equals(cart.getCustomer()))
            throw new CustomerNotMatchedException("Customer can not remove this cart " +
                    "because he/she not owns it");

        cartRepository.deleteById(cartId);

        //delete the orderItem with cart
        orderItemService.removeOrderItemForCart(cart);
    }
}
