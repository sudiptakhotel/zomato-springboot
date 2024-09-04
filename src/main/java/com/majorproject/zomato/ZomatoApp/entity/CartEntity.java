package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"restaurant" , "orderItems"})
@EqualsAndHashCode(exclude = {"restaurant" , "orderItems"})
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CustomerEntity customer;

    @OneToOne
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    //TODO : create a field foodAmount and delivery charge
    private Double deliverFee = 0.0;
    private Double totalAmount = 0.0;
    private Double foodAmount = 0.0;

    private Point dropOffLocation;

    private Boolean validCart = true; //make it false when customer plaOrder


}
