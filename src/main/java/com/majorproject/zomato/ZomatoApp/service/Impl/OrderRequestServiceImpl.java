package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.OrderItemDTO;
import com.majorproject.zomato.ZomatoApp.dto.OrderRequestDTO;
import com.majorproject.zomato.ZomatoApp.dto.PlaceOrderDTO;
import com.majorproject.zomato.ZomatoApp.entity.CartEntity;
import com.majorproject.zomato.ZomatoApp.entity.CustomerEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderItemEntity;
import com.majorproject.zomato.ZomatoApp.entity.OrderRequestEntity;
import com.majorproject.zomato.ZomatoApp.entity.enums.OrderRequestStatus;
import com.majorproject.zomato.ZomatoApp.exception.CustomerNotMatchedException;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.CartRepository;
import com.majorproject.zomato.ZomatoApp.repository.OrderRequestRepository;
import com.majorproject.zomato.ZomatoApp.service.CartRemovalService;
import com.majorproject.zomato.ZomatoApp.service.CustomerService;
import com.majorproject.zomato.ZomatoApp.service.OrderRequestService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderRequestServiceImpl implements OrderRequestService {

    private final OrderRequestRepository orderRequestRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final CustomerService customerService;


    @Override
    public OrderRequestEntity getOrderRequest(Long orderRequestId) {

        return orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Order request is not " +
                        "found with orderRequestId: "+orderRequestId));
    }

    @Override
    public OrderRequestEntity saveOrderRequest(OrderRequestEntity orderRequest) {

        return orderRequestRepository.save(orderRequest);
    }
}
