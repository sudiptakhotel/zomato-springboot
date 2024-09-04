package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.MenuItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.OrderItemRepository;
import com.majorproject.zomato.ZomatoApp.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItemEntity createOrderItem(MenuItemEntity menuItem, Integer quantity , CartEntity cart) {

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setMenuItem(menuItem);
        orderItemEntity.setItemName(menuItem.getName());
        orderItemEntity.setItemPrice(menuItem.getPrice());
        orderItemEntity.setQuantity(quantity);
        orderItemEntity.setCart(cart);

        return orderItemRepository.save(orderItemEntity);

    }

    @Override
    public OrderItemEntity isItemPresent(String itemName , CartEntity cart) {

        List<OrderItemEntity> orderItemEntityList = cart.getOrderItems();

        return orderItemEntityList.stream()
                .filter(orderItemEntity -> orderItemEntity.getItemName().equals(itemName))
                .findAny()
                .orElse(null);
    }

    @Override
    public void increaseOrderQuantity(OrderItemEntity orderItem, Integer quantity , CartEntity cart) {

        orderItem.setQuantity(orderItem.getQuantity() + quantity);
        orderItemRepository.save(orderItem);


    }

    @Override
    public void removeOrderItemForCart(CartEntity cart) {

        List<OrderItemEntity> orderItem = orderItemRepository.findByCart(cart)
                .orElseThrow(() -> new ResourceNotFoundException("Order item is not found" +
                        " with cartId : "+cart.getId()));

        //delete this orderItem
        orderItem
                .forEach(orderItemRepository::delete);

    }

    @Override
    public List<OrderItemEntity> getOrderItemsByOrderRequest(OrderRequestEntity orderRequest) {

        return orderItemRepository.findByOrderRequest(orderRequest)
                .orElse(null);
    }


}
