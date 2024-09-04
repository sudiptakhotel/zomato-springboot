package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.entity.enums.OrderStatus;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.repository.OrderRepository;
import com.majorproject.zomato.ZomatoApp.service.OrderService;
import com.majorproject.zomato.ZomatoApp.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final SmsService smsService;

    @Override
    public OrderEntity createOrder(OrderRequestEntity orderRequest) {

        List<OrderItemEntity> orderItemEntityList = new ArrayList<>(orderRequest.getOrderItems());

        OrderEntity order = new OrderEntity();
        order.setOrderItems(orderItemEntityList);
        order.setAmount(orderRequest.getAmount());
        order.setFoodAmount(orderRequest.getFoodAmount());
        order.setDeliveryFee(orderRequest.getDeliveryFee());
        order.setRestaurant(orderRequest.getRestaurant());
        order.setCustomer(orderRequest.getCustomer());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setRestaurantLocation(orderRequest.getRestaurantLocation());
        order.setDropOffLocation(orderRequest.getDropOffLocation());
        order.setStatus(OrderStatus.CONFIRMED);
        order.setOtp(generateRandomOTP());
        order.setRestaurantOTP(generateRandomRestaurantOTP());
        order.setPromo(orderRequest.getPromo());
        order.setPromoApplied(orderRequest.getPromoApplied());
        return orderRepository.save(order);
    }

    @Override
    public OrderEntity getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with " +
                        "id "+orderId));
    }

    @Override
    public OrderEntity saveOrder(OrderEntity order) {

        return orderRepository.save(order);
    }

    @Override
    public String getRestaurantOtp(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with " +
                        "orderId : "+orderId));

        return order.getRestaurantOTP();
    }

    @Override
    public String getOtp(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with " +
                        "orderId : "+orderId));

        return order.getOtp();
    }

    @Override
    public String generateRandomRestaurantOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);

        return String.valueOf(otp);
    }

    @Override
    public String generateRandomOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);

        return String.valueOf(otp);
    }

    @Override
    public OrderEntity updateOrderStatus(OrderEntity order, OrderStatus orderStatus) {

        order.setStatus(orderStatus);

        //update order status to customer by sms
        try {
            smsService.sendSms(order.getCustomer().getUser().getPhoneNumber() ,
                    "Hi! "+order.getCustomer().getUser().getName()+" thank you" +
                            " for placing order . Your order status is : "+order.getStatus().toString());
        }catch (Exception exception) {
            log.error("Exception occurred in twilio server , exception details : {}", exception.getMessage());
        }

        return orderRepository.save(order);
    }

    @Override
    public List<OrderEntity> getOrdersByCustomer(CustomerEntity customer) {

        return orderRepository.findByCustomer(customer)
                .orElse(null);

    }

    @Override
    public List<OrderEntity> getOrdersByPartner(PartnerEntity partner) {

        return orderRepository.findByPartner(partner)
                .orElse(null);
    }

    @Override
    public List<OrderEntity> getOrdersByRestaurant(RestaurantEntity restaurant) {

        return orderRepository.findByRestaurant(restaurant)
                .orElse(null);
    }
}
