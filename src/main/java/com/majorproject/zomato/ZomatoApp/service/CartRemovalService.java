package com.majorproject.zomato.ZomatoApp.service;

import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;

public interface CartRemovalService {

    void removeCart(Long cartId , CustomerEntity customer);
}
