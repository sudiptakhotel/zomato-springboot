package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.*;
import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.entity.enums.OrderRequestStatus;
import com.majorproject.zomato.ZomatoApp.exception.InvalidOrderStatusException;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.exception.RestaurantClosedException;
import com.majorproject.zomato.ZomatoApp.repository.OrderRequestRepository;
import com.majorproject.zomato.ZomatoApp.repository.RestaurantRepository;
import com.majorproject.zomato.ZomatoApp.service.*;
import com.majorproject.zomato.ZomatoApp.strategy.PartnerMatchingStrategy;
import com.majorproject.zomato.ZomatoApp.strategy.StrategyImpl.PartnerMatchingNearestPartner;
import com.majorproject.zomato.ZomatoApp.strategy.strategyManager.PartnerStrategyManager;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    private final OrderRequestRepository orderRequestRepository;
    private final OrderService orderService;
    private final PartnerMatchingNearestPartner partnerMatchingNearestPartner;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final MenuItemService menuItemService;
    private final RestaurantWalletService restaurantWalletService;
    private final RestaurantWalletTransactionService restaurantWalletTransactionService;
    private final PartnerStrategyManager partnerStrategyManager;

    @Override
    public RestaurantEntity getRestaurantById(Long restaurantId) {

        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found " +
                        "with id "+restaurantId));
    }

    @Override
    public Page<MenuItemDTO> getMenuFromRestaurant(Long restaurantId, Pageable pageable , String sortBy) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found " +
                        "with id "+restaurantId));

        List<MenuItemEntity> allMenuItems = restaurant.getMenuItems();

        //sort the item by sortBy
        List<MenuItemEntity> sortedItem = allMenuItems.stream()
                .sorted(getComparator(sortBy))
                .toList();

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        //calculate the start and end index of the allowMenuItems
        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize , sortedItem.size());

        //if the start is beyond the total items then return empty list
        if(start >= sortedItem.size()) {
            return new PageImpl<>(Collections.emptyList() , pageable , sortedItem.size());
        }

        List<MenuItemEntity> pagedItem = sortedItem.subList(start , end);

        List<MenuItemDTO> pagedItemDTO = pagedItem.stream()
                .map(menuItem -> {
                    return modelMapper.map(menuItem , MenuItemDTO.class);
                }).toList();

        //create the page and return
        return new PageImpl<>(pagedItemDTO , pageable , sortedItem.size());

    }

    @Override
    public RestaurantDTO getRestaurant(Long restaurantId) {

        RestaurantEntity restaurant =  restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant is not " +
                        "found with id "+restaurantId));

        return modelMapper.map(restaurant , RestaurantDTO.class);
    }

    @Override
    @Transactional
    public RestaurantOrderDTO acceptOrderRequest(Long orderRequestId) throws MessagingException {

        OrderRequestEntity orderRequest = orderRequestRepository.findById(orderRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Order request not " +
                        "found with id : "+orderRequestId));


        RestaurantEntity restaurant = orderRequest.getRestaurant();
        CustomerEntity customer = orderRequest.getCustomer();

        if(!orderRequest.getStatus().equals(OrderRequestStatus.PENDING)) {
            throw new InvalidOrderStatusException("Order request can not be accepted " +
                    "because the order status is : "+orderRequest.getStatus());
        }

        //check for the restaurant if it is active or not
        if(!restaurant.getIsOpen()) {
            throw new RestaurantClosedException("Restaurant is closed now and will open " +
                    "at : "+restaurant.getClosingTime());
        }

        //set the status as CONFIRMED and save the orderRequest
        orderRequest.setStatus(OrderRequestStatus.CONFIRMED);
        OrderRequestEntity savedOrderRequest = orderRequestRepository.save(orderRequest);

        //create orderEntity
        OrderEntity order = orderService.createOrder(orderRequest);

        //create the Payment entity and add ride to the payment
        paymentService.createPayment(order);

        //TODO : email partners for the order
        List<PartnerEntity> partners = partnerStrategyManager.getPartnerMatchingStrategy(customer.getRating())
                .findAllMatchingPartner(orderRequest);

        List<String> partnerEmails = partners.stream()
                .map(partnerEntity -> partnerEntity.getUser().getEmail())
                .toList();

        String[] partnerEmailArray = partnerEmails.toArray(new String[0]);

        //populate the email of partners
        //send email to partner

        //create template model
        Map<String , Object> templateModel = new HashMap<>();
        templateModel.put("orderId", order.getId());
        templateModel.put("customerName", order.getCustomer().getUser().getName());
        templateModel.put("deliveryAddress", order.getDropOffLocation().toString()); // You can customize this to a more readable format
        templateModel.put("totalAmount", order.getAmount());
        String subject = "New Order #" + order.getId();

        emailService.sendOrderDetailsToPartner(partnerEmailArray
                , subject , "partner-order-details.html" , templateModel);


        RestaurantOrderDTO restaurantOrderDTO = modelMapper.map(order , RestaurantOrderDTO.class);

        List<OrderItemDTO> orderItemDTOS = savedOrderRequest.getOrderItems().stream()
                .map(orderItemEntity -> modelMapper.map(orderItemEntity , OrderItemDTO.class))
                .toList();

        restaurantOrderDTO.setOrderItems(orderItemDTOS);

        return restaurantOrderDTO;

    }

    @Override
    public RestaurantOrderDTO cancelOrderRequest(Long orderRequest) {
        return null;
    }

    @Override
    public List<OrderDTO> getALlOrders(Long restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not " +
                        "found with id : "+restaurantId));

        List<OrderEntity> orderEntityList = orderService.getOrdersByRestaurant(restaurant);

        return orderEntityList.stream()
                .map(order -> modelMapper.map(order , OrderDTO.class))
                .toList();
    }

    @Override
    public RestaurantDTO getProfile(Long restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found " +
                        "with id : "+restaurantId));

        return modelMapper.map(restaurant , RestaurantDTO.class);
    }

    @Override
    public List<MenuItemDTO> getMenuItems(Long restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not " +
                        "found with id : "+restaurantId));

        List<MenuItemEntity> menuItemEntities = menuItemService.getMenuItemsByRestaurant(restaurant);

        return menuItemEntities.stream()
                .map(menuItem -> modelMapper.map(menuItem , MenuItemDTO.class))
                .toList();
    }

    @Override
    public List<OrderRequestDTO> getAllOrderRequest(Long restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not " +
                        "found with id : "+restaurantId));

        List<OrderRequestEntity> orderRequestEntityList = orderRequestRepository
                .findByRestaurant(restaurant).orElse(null);

        return orderRequestEntityList.stream()
                .map(orderRequest -> modelMapper.map(orderRequest , OrderRequestDTO.class))
                .toList();
    }

    @Override
    public List<RestaurantWalletTransactionDTO> getAllWalletTransaction(Long restaurantId) {

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not " +
                        "found with id "+restaurantId));


        RestaurantWalletEntity restaurantWallet = restaurantWalletService.getRestaurantWallet(restaurant);

        List<RestaurantWalletTransactionEntity> restaurantWalletTransactionEntityList =
                restaurantWalletTransactionService.getAllWalletTransaction(restaurantWallet);

        return restaurantWalletTransactionEntityList.stream()
                .map(restaurantWalletTransaction -> modelMapper.map(restaurantWalletTransaction , RestaurantWalletTransactionDTO.class))
                .toList();

    }

    private Comparator<MenuItemEntity> getComparator(String sortBy) {

        return switch (sortBy) {
            case "name" -> Comparator.comparing(MenuItemEntity::getName);
            case "price" -> Comparator.comparing(MenuItemEntity::getPrice);
            case "description" -> Comparator.comparing(MenuItemEntity::getDescription);
            case "available" -> Comparator.comparing(MenuItemEntity::getAvailable);
            default -> Comparator.comparing(MenuItemEntity::getId);
        };

    }
}
