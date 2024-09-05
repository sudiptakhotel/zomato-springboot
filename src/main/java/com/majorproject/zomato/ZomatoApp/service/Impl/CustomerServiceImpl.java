package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.entity.enums.OrderRequestStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import com.majorproject.zomato.ZomatoApp.entity.enums.TransactionMethod;
import com.majorproject.zomato.ZomatoApp.exception.CustomerNotMatchedException;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.exception.RestaurantClosedException;
import com.majorproject.zomato.ZomatoApp.exception.RuntimeConflictException;
import com.majorproject.zomato.ZomatoApp.repository.CartRepository;
import com.majorproject.zomato.ZomatoApp.repository.CustomerRepository;
import com.majorproject.zomato.ZomatoApp.repository.OrderRequestRepository;
import com.majorproject.zomato.ZomatoApp.repository.UserRepository;
import com.majorproject.zomato.ZomatoApp.service.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RestaurantService restaurantService;
    private final CartRepository cartRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final PromoService promoService;
    private final RatingService ratingService;
    private final OrderItemService orderItemService;
    private final UserWalletTransactionService userWalletTransactionService;
    private final UserWalletService userWalletService;




    @Override
    public CustomerEntity getCustomer() {

        //TODO : fetch the logged in customer from Security context holder

        UserEntity user = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        return customerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("customer is not found " +
                        "with userId : "+user.getId()));

    }

    @Override
    public Page<MenuItemDTO> getMenuByRestaurant(Long restaurantId, Pageable pageable , String sortBy) {
        return restaurantService.getMenuFromRestaurant(restaurantId , pageable , sortBy);
    }

    @Override
    public CustomerEntity getCustomerById(Long customerId) {

        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found " +
                        "with id : "+customerId));
    }

    @Override
    @Transactional
    public OrderRequestDTO placeOrder(Long cartId , PlaceOrderDTO placeOrderDTO) throws MessagingException {

//        if customer has 0 balance in wallet and the payment mode he/she
//        chooses WALLET , then not allow to place Order

        CustomerEntity customer = getCustomer();
        UserEntity user = customer.getUser();
        UserWalletEntity wallet = userWalletService.getUserWallet(user);

        if (placeOrderDTO.getPaymentMethod().equals(PaymentMethod.WALLET) && wallet.getBalance()<=0.0) {

            throw new RuntimeConflictException("Customer can not place order with " +
                        "'WALLET' payment method because the balance in wallet is "+wallet.getBalance());
        }


        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("cart not found " +
                        "with cartId : "+cartId));

        RestaurantEntity restaurant = cart.getRestaurant();

        if(!cart.getCustomer().equals(customer))
            throw new CustomerNotMatchedException("Customer not matched");

        //check if restaurant is open / not
        LocalTime now = LocalTime.now();

        if (!restaurant.getIsOpen()) {
            throw new RestaurantClosedException("Restaurant is closed currently.");
        }

        // If the closing time is after midnight (e.g., 3:00 AM)
        if (restaurant.getClosingTime().isBefore(restaurant.getOpeningTime())) {
            // Check if the current time is before opening or after closing
            if (now.isBefore(restaurant.getOpeningTime()) && now.isAfter(restaurant.getClosingTime())) {
                throw new RestaurantClosedException("Restaurant is closed currently, closing time: " + restaurant.getClosingTime());
            }
        } else {
            // Normal business hours (closing time on the same day)
            if (now.isAfter(restaurant.getClosingTime())) {
                throw new RestaurantClosedException("Restaurant is closed currently, closing time: " + restaurant.getClosingTime());
            }
        }



        List<OrderItemEntity> orderItemEntities = new ArrayList<>(cart.getOrderItems());



        //get the promo if orderRequest has
        //update the totalBill if promo applied
        Double totalBill = cart.getTotalAmount();
        PromoEntity promo = null;

        if(placeOrderDTO.getPromoId() != null) {
            promo = promoService.getPromoById(placeOrderDTO.getPromoId());
            totalBill = promoService.applyPromo(promo.getId() , totalBill);
        }

        OrderRequestEntity orderRequest = new OrderRequestEntity();

        orderRequest.setCustomer(cart.getCustomer());
        orderRequest.setRestaurant(cart.getRestaurant());
        orderRequest.setAmount(totalBill);
        orderRequest.setFoodAmount(cart.getFoodAmount());
        orderRequest.setDeliveryFee(cart.getDeliverFee());
        orderRequest.setStatus(OrderRequestStatus.PENDING);
        orderRequest.setCartId(cartId);
        orderRequest.setPromo(promo);
        orderRequest.setPromoApplied(true);
        orderRequest.setOrderItems(orderItemEntities);
        orderRequest.setRestaurantLocation(cart.getRestaurant().getRestaurantLocation());
        orderRequest.setDropOffLocation(cart.getDropOffLocation());
        orderRequest.setPaymentMethod(placeOrderDTO.getPaymentMethod());

        //save the orderRequest
        OrderRequestEntity savedOrderRequest = orderRequestRepository.save(orderRequest);

        //loop through each OrderItem and set the OrderRequest
        orderItemEntities.forEach(orderItemEntity -> {
            orderItemEntity.setOrderRequest(savedOrderRequest);
            orderItemService.saveOrderItem(orderItemEntity);
        });



        //delete the cart once orderPlaced
        orderRequest.getOrderItems().forEach(orderItem -> orderItem.setCart(null));
        cartRepository.delete(cart);


        //TODO : sent email notification to restaurant to accept / cancel the order
        String restaurantEmail = restaurant.getEmail();
        String subject = "New Order Request # "+orderRequest.getId();

        //create the dynamic field-value of OrderRequest

        Map<String , Object> templateModel = new HashMap<>();
        templateModel.put("OrderRequestId" , orderRequest.getId());
        templateModel.put("OrderItem", orderRequest.getOrderItems());
        templateModel.put("Bill" , orderRequest.getFoodAmount());


        emailService.sendOrderDetailsToPartner(restaurantEmail , subject ,
                "restaurant-order-request-details.html" , templateModel);

        //set the required fields or OrderRequestDTO
        OrderRequestDTO orderDetails = modelMapper.map(savedOrderRequest , OrderRequestDTO.class);

        List<OrderItemDTO> itemList = savedOrderRequest.getOrderItems().stream()
                .map(orderItemEntity -> modelMapper.map(orderItemEntity , OrderItemDTO.class))
                .toList();

        orderDetails.setOrderItems(itemList);
        if(promo != null) {
            orderDetails.setDiscountPercentage(orderRequest.getPromo().getDiscountPercentage().toString()+" %");
        }
        orderDetails.setRestaurantName(savedOrderRequest.getRestaurant().getName());
        return orderDetails;


    }

    @Override
    public CustomerOrderDTO getOrderById(Long orderId) {

        OrderEntity order = orderService.getOrderById(orderId);

        CustomerOrderDTO customerOrderDTO = modelMapper.map(order , CustomerOrderDTO.class);
        customerOrderDTO.setRestaurantName(order.getRestaurant().getName());
        customerOrderDTO.setPartnerName(order.getPartner().getUser().getName());

        return customerOrderDTO;
    }

    @Override
    public CustomerEntity createCustomer(UserEntity savedUser) {

        CustomerEntity customer = new CustomerEntity();
        customer.setUser(savedUser);
        return customerRepository.save(customer);
    }

    @Override
    public List<CustomerPromoDTO> getEligiblePromos(Long cartId) {

        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found " +
                        "with id "+cartId));
        return promoService.getEligiblePromos(cart.getTotalAmount());
    }

    @Override
    public PartnerDTO ratePartner(Long orderId, Double rating) {

        OrderEntity order = orderService.getOrderById(orderId);

        return ratingService.ratePartner(order , rating);
    }

    @Override
    public RestaurantDTO rateRestaurant(Long orderId, Double rating) {

        OrderEntity order = orderService.getOrderById(orderId);

        return ratingService.rateRestaurant(order , rating);
    }

    @Override
    public CustomerDTO getMyProfile(Long customerId) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found " +
                        "with id"+customerId));

        return modelMapper.map(customer , CustomerDTO.class);

    }

    @Override
    public List<OrderDTO> getAllMyOrders(Long customerId) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found " +
                        "with id"+customerId));

        List<OrderEntity> orderEntityList = orderService.getOrdersByCustomer(customer);

        return orderEntityList.stream()
                .map(order -> modelMapper.map(order , OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDTO> getOrderItemsByOrderRequestId(Long orderRequestId) {

        OrderRequestEntity orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Order request not found " +
                        "with id : "+orderRequestId));

        List<OrderItemEntity> orderItemEntityList = orderItemService.getOrderItemsByOrderRequest(orderRequest);

        return orderItemEntityList.stream()
                .map(orderItemEntity -> modelMapper.map(orderItemEntity , OrderItemDTO.class))
                .toList();
    }

    @Override
    public List<OrderRequestDTO> getAllMyOrderRequests(Long customerId) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not " +
                        "found with id : "+customerId));

        List<OrderRequestEntity> orderRequestEntityList = orderRequestRepository
                .findByCustomer(customer).orElse(null);

        return orderRequestEntityList.stream()
                .map(orderRequest -> modelMapper.map(orderRequest , OrderRequestDTO.class))
                .toList();
    }

    @Override
    public List<UserWalletTransactionDTO> getAllMyWalletTransaction(Long customerId) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not " +
                        "found with id "+customerId));

        UserEntity user = customer.getUser();

        UserWalletEntity userWallet = userWalletService.getUserWallet(user);

        List<UserWalletTransactionEntity> userWalletTransactionEntityList =
                userWalletTransactionService.getAllUserWalletTransaction(userWallet);

        return userWalletTransactionEntityList.stream()
                .map(userWalletTransactionEntity -> modelMapper.map(userWalletTransactionEntity , UserWalletTransactionDTO.class))
                .toList();

    }

    @Override
    public WalletDTO addBalanceToWallet(Long customerId, Double amount , String transactionId) {

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found " +
                        "with id : "+customerId));

        UserEntity user = customer.getUser();

        UserWalletEntity wallet = userWalletService.getUserWallet(user);

        UserWalletEntity userWallet = userWalletService.addMoneyToUsersWallet(user , amount , transactionId , null , TransactionMethod.BANKING);

        return modelMapper.map(userWallet , WalletDTO.class);
    }
}
