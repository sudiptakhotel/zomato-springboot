package com.majorproject.zomato.ZomatoApp.entity;

import com.majorproject.zomato.ZomatoApp.entity.enums.OrderRequestStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "order_request" ,
indexes = {
        @Index(name = "idx_order_request_restaurant" , columnList = "restaurant_id") ,
        @Index(name = "idx_order_request_customer" , columnList = "customer_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(exclude = "orderItems")
public class OrderRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cartId; //only storing the cartId not the whole cart.
    private Double foodAmount;
    private Double deliveryFee;
    private Double amount;

    @ManyToOne
    private RestaurantEntity restaurant;

    @Enumerated(EnumType.STRING)
    private OrderRequestStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    private LocalDateTime requestedTime;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @ManyToOne
    private CustomerEntity customer;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    private Point restaurantLocation;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    private Point dropOffLocation;

    @OneToOne
    private PromoEntity promo; //one order can have one promo at max

    private Boolean promoApplied = false;

}
