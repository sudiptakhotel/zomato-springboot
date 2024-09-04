package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.dto.CustomerDTO;
import com.majorproject.zomato.ZomatoApp.dto.PartnerDTO;
import com.majorproject.zomato.ZomatoApp.dto.PartnerOrderDTO;
import com.majorproject.zomato.ZomatoApp.dto.RestaurantDTO;
import com.majorproject.zomato.ZomatoApp.entity.*;
import com.majorproject.zomato.ZomatoApp.exception.ResourceNotFoundException;
import com.majorproject.zomato.ZomatoApp.exception.RuntimeConflictException;
import com.majorproject.zomato.ZomatoApp.repository.CustomerRepository;
import com.majorproject.zomato.ZomatoApp.repository.PartnerRepository;
import com.majorproject.zomato.ZomatoApp.repository.RatingRepository;
import com.majorproject.zomato.ZomatoApp.repository.RestaurantRepository;
import com.majorproject.zomato.ZomatoApp.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final PartnerRepository partnerRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public PartnerDTO ratePartner(OrderEntity order , Double rating) {

        RatingEntity ratingEntity = ratingRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Rating object not found for order, " +
                        "with orderId : "+order.getId()));

        //check if the rating is already applied
        if(ratingEntity.getPartnerRating() != null)
            throw new RuntimeConflictException("Rating is already provided " +
                    "for orderId : "+order.getId());

        PartnerEntity partner = order.getPartner();

        ratingEntity.setPartnerRating(rating);
        ratingEntity.setPartner(partner);

        ratingRepository.save(ratingEntity);

        //every time ratePartner call we will update the average rating of partner

        Double newRating = ratingRepository.findByPartner(partner)
                .stream()
                .mapToDouble(RatingEntity::getPartnerRating)
                .average()
                .orElse(0.0);

        partner.setRating(newRating);
        PartnerEntity savedPartner = partnerRepository.save(partner);
        return modelMapper.map(savedPartner , PartnerDTO.class);

    }

    @Override
    public CustomerDTO rateCustomer(OrderEntity order, Double rating) {

        RatingEntity ratingEntity = ratingRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Rating object not found for order, " +
                        "with orderId : "+order.getId()));

        //check if the rating is already applied
        if(ratingEntity.getCustomerRating() != null)
            throw new RuntimeConflictException("Rating is already provided " +
                    "for orderId : "+order.getId());

        CustomerEntity customer = order.getCustomer();

        ratingEntity.setCustomer(customer);
        ratingEntity.setCustomerRating(rating);
        ratingRepository.save(ratingEntity);

        //every time ratePartner call we will update the average rating of partner

        Double newRating = ratingRepository.findByCustomer(customer)
                .stream()
                .mapToDouble(RatingEntity::getPartnerRating)
                .average()
                .orElse(0.0);

        customer.setRating(newRating);
        CustomerEntity savedCustomer = customerRepository.save(customer);

        return modelMapper.map(savedCustomer , CustomerDTO.class);


    }

    @Override
    public RestaurantDTO rateRestaurant(OrderEntity order, Double rating) {

        RatingEntity ratingEntity = ratingRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Rating object not found for order, " +
                        "with orderId : "+order.getId()));

        //check if the rating is already applied
        if(ratingEntity.getRestaurantRating() != null)
            throw new RuntimeConflictException("Rating is already provided " +
                    "for orderId : "+order.getId());

        RestaurantEntity restaurant = order.getRestaurant();

        ratingEntity.setRestaurant(restaurant);
        ratingEntity.setRestaurantRating(rating);
        ratingRepository.save(ratingEntity);

        //every time ratePartner call we will update the average rating of partner

        Double newRating = ratingRepository.findByRestaurant(restaurant)
                .stream()
                .mapToDouble(RatingEntity::getPartnerRating)
                .average()
                .orElse(0.0);

        restaurant.setRating(newRating);
        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        return modelMapper.map(savedRestaurant , RestaurantDTO.class);

    }

    @Override
    public void createRating(OrderEntity order, PartnerEntity partner, CustomerEntity customer, RestaurantEntity restaurant) {

        RatingEntity rating = RatingEntity
                .builder()
                .order(order)
                .partner(partner)
                .customer(customer)
                .restaurant(restaurant)
                .build();

        ratingRepository.save(rating);
    }

}
