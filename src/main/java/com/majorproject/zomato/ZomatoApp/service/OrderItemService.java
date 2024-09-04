package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.MenuItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;

import java.util.List;

public interface OrderItemService {

    OrderItemEntity createOrderItem(MenuItemEntity menuItem , Integer quantity , CartEntity cart);

    OrderItemEntity isItemPresent(String itemName , CartEntity cart);

    void increaseOrderQuantity(OrderItemEntity orderItem, Integer quantity , CartEntity cart);

    void removeOrderItemForCart(CartEntity cart);

    List<OrderItemEntity> getOrderItemsByOrderRequest(OrderRequestEntity orderRequest);
}
