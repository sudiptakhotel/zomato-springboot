package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.CartDTO;
import com.majorproject.zomato.ZomatoApp.dto.CartItemDTO;
import com.majorproject.zomato.ZomatoApp.dto.MenuItemDTO;
import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.entity.enums.ItemAction;
import com.majorproject.zomato.ZomatoApp.exception.*;
import com.majorproject.zomato.ZomatoApp.repository.CartRepository;
import com.majorproject.zomato.ZomatoApp.service.*;
import com.majorproject.zomato.ZomatoApp.strategy.strategyManager.DeliveryFeeStrategyManager;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final ModelMapper modelMapper;
    private final OrderItemService orderItemService;
    private final DeliveryFeeStrategyManager deliveryFeeStrategy;

    @Override
    @Transactional
    public CartDTO addItemToCart(CartItemDTO cartItemDTO) {


        CustomerEntity customer = customerService.getCustomer();
        RestaurantEntity restaurant = restaurantService.getRestaurantById(cartItemDTO.getRestaurantId());

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


        //check if the customer has an active cart / not
        CartEntity cart = cartRepository.findByCustomer(customer)
                .orElse(null);

        //check if the cart is valid or not
        if(cart!= null && !cart.getValidCart())
            throw new CartInvalidException("Cart is not valid , cartId : "+cart.getId());


        //if cart is null create a new cart and assign the customer
        if(cart == null) {
            cart = createCart(customer , restaurant , cartItemDTO);
        }

        //we will allow customer to add items from the same restaurant into a single cart
        else if(!cart.getRestaurant().getId().equals(cartItemDTO.getRestaurantId()))
            throw new RestaurantNotMatchedException("Restaurant is different from " +
                    " the existing cart , restaurantId in cart :"+cart.getRestaurant().getId());




        //create OrderItem for the cart
        MenuItemDTO menuItemDTO = menuItemService.getMenuItemById(cartItemDTO.getMenuItemId());
        MenuItemEntity menuItem = modelMapper.map(menuItemDTO , MenuItemEntity.class);

        //if customer is adding same menu Item than increase the quantity
        OrderItemEntity orderItem = orderItemService.isItemPresent(menuItem.getName() , cart);
        if(orderItem != null) {
            orderItemService.increaseOrderQuantity(orderItem , cartItemDTO.getQuantity() , cart);
        }
        else {
            orderItem = orderItemService.createOrderItem(menuItem , cartItemDTO.getQuantity() , cart);
            cart.getOrderItems().add(orderItem);
        }

        //update total amount
        CartEntity savedCart = updateAmount(cart , orderItem.getItemPrice()*cartItemDTO.getQuantity()
                , ItemAction.ITEM_ADDED);


        return modelMapper.map(savedCart , CartDTO.class);
    }

    @Override
    @Transactional
    public CartEntity removeItemFromCart(Long orderItemId , CartEntity cart) {

        List<OrderItemEntity> orderItemEntities = cart.getOrderItems();

        OrderItemEntity orderItem = orderItemEntities.stream()
                .filter(orderItemEntity -> orderItemEntity.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item is not " +
                        "found with id : "+orderItemId));

        orderItemEntities.remove(orderItem);

        return cartRepository.save(cart);
    }

    @Override
    public CartDTO getTotalCartAmount(Long cartId) {
        return null;
    }

    @Override
    public CartEntity getCartByCustomer(CustomerEntity customer) {

        return cartRepository.findByCustomer(customer)
                .orElse(null);

    }

    @Override
    public CartDTO getCartById(Long cartId) {

        CustomerEntity customer = customerService.getCustomer();

        CartEntity cart =  cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("cart not found with " +
                        "id "+cartId));

        //check if same customer is requesting
        if(!cart.getCustomer().equals(customer))
            throw new CustomerNotMatchedException("Can not perform the action because " +
                    "customer with id :"+customer.getId()+" is not owned the cart");

        //if not item is present in the cart show this to customer
        if(cart.getOrderItems().isEmpty()) {
            throw new CartHasNoItemException("Cart has no item");
        }

        return modelMapper.map(cart , CartDTO.class);
    }

    @Override
    @Transactional
    public CartEntity createCart(CustomerEntity customer, RestaurantEntity restaurant , CartItemDTO cartItemDTO) {

        CartEntity cart = new CartEntity();

        cart.setCustomer(customer);
        cart.setRestaurant(restaurant);
        cart.setDropOffLocation(modelMapper.map(cartItemDTO.getDropOffLocation() , Point.class));


        //calculate the delivery fee
        Double deliveryFee = deliveryFeeStrategy.getDeliveryFeeCalculationStrategy(cart)
                        .calculateDeliveryFee(cart);

        cart.setDeliverFee(deliveryFee);
        cart.setTotalAmount(deliveryFee);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    @Transactional
    public CartEntity updateAmount(CartEntity cart, Double amount, ItemAction itemAction) {

        Double currentFoodAmount = cart.getFoodAmount();
        Double currentTotalAmount = cart.getTotalAmount();

        if(itemAction.equals(ItemAction.ITEM_ADDED)) {
            cart.setTotalAmount(currentTotalAmount + amount);
            cart.setFoodAmount(currentFoodAmount + amount);
        }
        else {
            cart.setFoodAmount(currentFoodAmount - amount);
            cart.setTotalAmount(currentTotalAmount - amount);
        }

        return cartRepository.save(cart);

    }

    @Override
    @Transactional
    public CartDTO increaseQuantity(Long orderItemId, Long cartId , Integer quantity , Long customerId) {

        //check if it is a valid customer
        CustomerEntity customer = customerService.getCustomer();

        if(!customer.equals(customerService.getCustomerById(customerId)))
            throw new CustomerNotMatchedException("Can not perform the action because " +
                    "customer with id :"+customerId+" is not owned the cart");

        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart is not found " +
                        "with id "+cartId));

        //fetch the orderItem with given orderItemId
        List<OrderItemEntity> orderItemEntities = cart.getOrderItems();

        OrderItemEntity orderItem = orderItemEntities.stream()
                .filter(orderItemEntity -> orderItemEntity.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item is not " +
                        "found with id : "+orderItemId));

        orderItem.setQuantity(orderItem.getQuantity() + quantity);
        CartEntity savedCart = updateAmount(cart , orderItem.getItemPrice() * quantity , ItemAction.ITEM_ADDED);

        return modelMapper.map(savedCart , CartDTO.class);

    }

    @Override
    @Transactional
    public CartDTO decreaseQuantity(Long orderItemId, Long cartId, Integer quantity , Long customerId) {

        // Check if it is a valid customer
        CustomerEntity customer = customerService.getCustomer();
        if (!customer.equals(customerService.getCustomerById(customerId))) {
            throw new CustomerNotMatchedException("Cannot perform the action because " +
                    "customer with id: " + customerId + " does not own the cart");
        }

        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart is not found " +
                        "with id " + cartId));

        // Fetch the orderItem with given orderItemId
        OrderItemEntity orderItem = cart.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Order item is not " +
                        "found with id: " + orderItemId));

        int currentQuantity = orderItem.getQuantity();

        if (quantity > currentQuantity) {
            throw new RuntimeException("Item's quantity in cart is less than the given " +
                    "quantity. Item's quantity: " + currentQuantity);
        }

        int changedQuantity = currentQuantity - quantity;
        orderItem.setQuantity(changedQuantity);

        // Persist the cart after updating the quantity
        cart = updateAmount(cart, orderItem.getItemPrice() * quantity, ItemAction.ITEM_REMOVED);

        if (changedQuantity == 0) {
            // Remove the order item from the cart
            removeItemFromCart(orderItemId ,cart);
            // Persist the cart to ensure changes are saved
        }
        if(cart.getOrderItems().isEmpty()) {

            //delete the cart if there is no orderItem
            cartRepository.delete(cart);

            //I need to delete OrderItem
            orderItemService.removeOrderItemForCart(cart);
            throw new CartHasNoItemException("Cart has no item");
        }


        return modelMapper.map(cart, CartDTO.class);
    }


    @Override
    public CartDTO createNewCart(CartItemDTO cartItemDTO) {


        CustomerEntity customer = customerService.getCustomer();
        RestaurantEntity restaurant = restaurantService.getRestaurantById(cartItemDTO.getRestaurantId());

        if(cartRepository.findByCustomer(customer).isPresent()) {
            throw new DuplicateCartFoundException("An active cart is already present ");
        }
        CartEntity newCart = createCart(customer , restaurant , cartItemDTO);
        return addItemToCart(cartItemDTO);
    }
}
