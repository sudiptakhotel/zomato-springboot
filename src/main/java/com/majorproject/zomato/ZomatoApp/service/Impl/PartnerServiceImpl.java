package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.entity.enums.OrderStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.exception.*;
import com.majorproject.zomato.ZomatoApp.repository.PartnerRepository;
import com.majorproject.zomato.ZomatoApp.service.*;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final UserService userService;
    private final RatingService ratingService;
    private final UserWalletService userWalletService;
    private final UserWalletTransactionService userWalletTransactionService;

    @Override
    @Transactional
    public PartnerOrderDTO acceptOrder(Long orderId) {

        PartnerEntity partner = getPartner();
        UserEntity user = partner.getUser();
        OrderEntity order = orderService.getOrderById(orderId);
        UserWalletEntity wallet = userWalletService.getUserWallet(user);

        //if the partner's wallet has less than 0.0 balance he/she can not accept
        //CASH payment method order

        if (order.getPaymentMethod().equals(PaymentMethod.CASH ) && wallet.getBalance()<=0.0) {
            throw new RuntimeConflictException("Partner can not accept this order " +
                    "because he/she has balance in wallet : "+wallet.getBalance());
        }



        //if partner is not available he/she can not take order
        if(!partner.getAvailable())
            throw new PartnerNotAvailableException("Partner is not available , " +
                    "partnerId : "+partner.getId());

        //check the orderStatus is other than CONFIRMED
        if(!order.getStatus().equals(OrderStatus.CONFIRMED))
            throw new InvalidOrderStatusException("Partner can not accept order because " +
                    "order status is "+order.getStatus());

        //assign partner to the order
        order.setPartner(partner);

        //update the order status and save
        OrderEntity savedOrder = orderService.updateOrderStatus(order , OrderStatus.PARTNER_ASSIGNED);

        //set the partner as unavailable and save partner
        partner.setAvailable(false);
        partnerRepository.save(partner);

        return modelMapper.map(savedOrder , PartnerOrderDTO.class);

    }

    private PartnerEntity getPartner() {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return partnerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with " +
                        "user id :"+user.getId()));
    }

    @Override
    @Transactional
    public PartnerOrderDTO cancelOrder(Long orderId) {

        PartnerEntity partner = getPartner();

        OrderEntity order = orderService.getOrderById(orderId);

        //check if partner accepted this order previously
        if(!partner.equals(order.getPartner()))
            throw new InvalidPartnerException("Partner can not cancel the order because he/she " +
                    "not owns the order");
        if(!order.getStatus().equals(OrderStatus.PARTNER_ASSIGNED))
            throw new InvalidOrderStatusException("Partner can not cancel this order " +
                    "because order status is : "+order.getStatus());

        OrderEntity savedOrder = orderService.updateOrderStatus(order , OrderStatus.CANCELLED);

        //make the partner available and save to database
        partner.setAvailable(true);
        partnerRepository.save(partner);

        return modelMapper.map(savedOrder , PartnerOrderDTO.class);

    }

    @Override
    @Transactional
    public PartnerOrderDTO pickupOrderFromRestaurant(Long orderId , String restaurantOtp) {

        PartnerEntity partner = getPartner();

        OrderEntity order = orderService.getOrderById(orderId);

        //check if partner accepted this order previously
        if(!partner.equals(order.getPartner()))
            throw new InvalidPartnerException("Partner can not pick up the order because he/she " +
                    "not owns the order");
        if(!order.getStatus().equals(OrderStatus.PARTNER_ASSIGNED))
            throw new InvalidOrderStatusException("Partner can not pick up this order " +
                    "because order status is : "+order.getStatus());
        if(!orderService.getRestaurantOtp(orderId).equals(restaurantOtp))
            throw new WrongRestaurantOtpException("Otp does not match");

        //update order status
        OrderEntity savedOrder = orderService.updateOrderStatus(order , OrderStatus.PICKED_UP);
        return modelMapper.map(savedOrder , PartnerOrderDTO.class);
    }

    @Override
    @Transactional
    public PartnerOrderDTO startOrderRide(Long orderId) {

        PartnerEntity partner = getPartner();

        OrderEntity order = orderService.getOrderById(orderId);
        CustomerEntity customer = order.getCustomer();
        RestaurantEntity restaurant = order.getRestaurant();

        //check if partner accepted this order previously
        if(!partner.equals(order.getPartner()))
            throw new InvalidPartnerException("Partner can not start order ride because he/she " +
                    "not owns the order");
        if(!order.getStatus().equals(OrderStatus.PICKED_UP))
            throw new InvalidOrderStatusException("Partner can not start this order " +
                    "because order status is : "+order.getStatus());

        //create a RatingEntity
        ratingService.createRating(order , partner , customer , restaurant);


        OrderEntity savedOrder = orderService.updateOrderStatus(order , OrderStatus.OUT_FOR_DELIVERY);
        return modelMapper.map(savedOrder , PartnerOrderDTO.class);
    }

    @Override
    @Transactional
    public PartnerOrderDTO endOrderRide(Long orderId , String otp) {

        PartnerEntity partner = getPartner();

        OrderEntity order = orderService.getOrderById(orderId);

        //check if partner accepted this order previously
        if(!partner.equals(order.getPartner()))
            throw new InvalidPartnerException("Partner can not end order ride because he/she " +
                    "not owns the order");
        if(!order.getStatus().equals(OrderStatus.OUT_FOR_DELIVERY))
            throw new InvalidOrderStatusException("Partner can not end this order ride  " +
                    "because order status is : "+order.getStatus());
        if(!order.getOtp().equals(otp))
            throw new WrongCustomerOtpException("Otp does not match");

        order.setDeliveredTime(LocalDateTime.now());
        OrderEntity savedOrder = orderService.updateOrderStatus(order , OrderStatus.DELIVERED);

        //make the partner available and save partner to database
        partner.setAvailable(true);
        partnerRepository.save(partner);

        //process payment
        paymentService.processPayment(order);

        return modelMapper.map(savedOrder , PartnerOrderDTO.class);

    }

    @Override
    public PartnerEntity getPartnerByUserId(Long userId) {

        UserEntity user = userService.getUserById(userId);

        PartnerEntity partner = partnerRepository.findByUser(user)
                .orElse(null);

        return partner;
    }

    @Override
    public PartnerEntity createPartner(UserEntity savedUser, PointDTO pointDTO) {

        Point partnerLocation = modelMapper.map(pointDTO , Point.class);

        PartnerEntity partner =  PartnerEntity.builder()
                .user(savedUser)
                .available(true)
                .currentLocation(partnerLocation)
                .build();

        return partnerRepository.save(partner);

    }

    @Override
    public CustomerDTO rateCustomer(Long orderId, Double rating) {

        OrderEntity order = orderService.getOrderById(orderId);

        return ratingService.rateCustomer(order , rating);
    }

    @Override
    public PartnerDTO getMyProfile(Long partnerId) {

        PartnerEntity partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not " +
                        "found with id : "+partnerId));

        return modelMapper.map(partner , PartnerDTO.class);
    }

    @Override
    public List<OrderDTO> getAllOrders(Long partnerId) {

        PartnerEntity partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not " +
                        "found with id : "+partnerId));

        List<OrderEntity> orderEntityList = orderService.getOrdersByPartner(partner);

        return orderEntityList.stream()
                .map(order -> modelMapper.map(order , OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserWalletTransactionDTO> getAllMyWalletTransaction(Long partnerId) {

        PartnerEntity partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not " +
                        "found with id "+partnerId));

        UserEntity user = partner.getUser();

        UserWalletEntity userWallet = userWalletService.getUserWallet(user);

        List<UserWalletTransactionEntity> userWalletTransactionEntityList =
                userWalletTransactionService.getAllUserWalletTransaction(userWallet);

        return userWalletTransactionEntityList.stream()
                .map(userWalletTransactionEntity -> modelMapper.map(userWalletTransactionEntity , UserWalletTransactionDTO.class))
                .toList();
    }

    @Override
    public WalletDTO addBalanceToWallet(Long partnerId, Double amount, String transactionId) {

        PartnerEntity partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found " +
                        "with id : "+partnerId));

        UserEntity user = partner.getUser();

        UserWalletEntity wallet = userWalletService.getUserWallet(user);

        UserWalletEntity userWallet = userWalletService.addMoneyToUsersWallet(user , amount , transactionId , null , TransactionMethod.BANKING);

        return modelMapper.map(userWallet , WalletDTO.class);
    }
}
