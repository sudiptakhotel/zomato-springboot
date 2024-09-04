package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rating" ,
indexes = {
        @Index(name = "idx_rating_customer" , columnList = "customer_id"),
        @Index(name = "idx_rating_partner" , columnList = "partner_id"),
        @Index(name = "idx_rating_restaurant" , columnList = "restaurant_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private OrderEntity order;

    //I am adding customer , partner and restaurant so that I can
    //quickly call the rating of all customer / partner / restaurant

    @ManyToOne
    private CustomerEntity customer;

    @ManyToOne
    private PartnerEntity partner;

    @ManyToOne
    private RestaurantEntity restaurant;

    private Double customerRating;
    private Double restaurantRating;
    private Double partnerRating;

}
