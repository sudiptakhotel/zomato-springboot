package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.MenuItemDTO;
import com.majorproject.zomato.ZomatoApp.entity.MenuItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.RestaurantEntity;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.MenuItemRepository;
import com.majorproject.zomato.ZomatoApp.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public MenuItemDTO getMenuItemById(Long menuItemId) {

        MenuItemEntity menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item is not " +
                        "found with id "+menuItemId));

        return modelMapper.map(menuItem , MenuItemDTO.class);
    }

    @Override
    public List<MenuItemEntity> getMenuItemsByRestaurant(RestaurantEntity restaurant) {

        return menuItemRepository.findByRestaurant(restaurant)
                .orElse(null);
    }
}
