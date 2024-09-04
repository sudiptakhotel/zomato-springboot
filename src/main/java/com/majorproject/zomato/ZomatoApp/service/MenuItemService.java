package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.dto.MenuItemDTO;
import com.majorproject.zomato.ZomatoApp.entity.MenuItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;

import java.util.List;

public interface MenuItemService {

    MenuItemDTO getMenuItemById(Long menuItemId);

    List<MenuItemEntity> getMenuItemsByRestaurant(RestaurantEntity restaurant);
}
