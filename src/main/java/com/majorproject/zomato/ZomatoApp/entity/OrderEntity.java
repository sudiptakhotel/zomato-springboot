package com.majorproject.zomato.ZomatoApp.entity;

import com.majorproject.zomato.ZomatoApp.entity.enums.OrderStatus;
import com.majorproject.zomato.ZomatoApp.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "app_order" ,
        indexes = {
                @Index(name = "idx_order_customer" , columnList = "customer_id") ,
                @Index(name = "idx_order_partner" , columnList = "partner_id") ,
                @Index(name = "idx_order_restaurant" , columnList = "restaurant_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"orderItems"})
@EqualsAndHashCode(exclude = {"orderItems"})
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private CustomerEntity customer;

    @ManyToOne
    private PartnerEntity partner;

    @OneToMany(fetch = FetchType.EAGER)
    private List<OrderItemEntity> orderItems;

    private Double amount;
    private Double deliveryFee;
    private Double foodAmount;

    @ManyToOne
    private RestaurantEntity restaurant;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    private LocalDateTime createdTime;//when restaurant accepts orderRequest

    private LocalDateTime deliveredTime; //when its delivered

    @Column(columnDefinition = "Geometry(Point , 4326)")
    private Point restaurantLocation;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    private Point dropOffLocation;

    private String otp; // customer will tell delivery partner
    private String restaurantOTP; // delivery partner will tell restaurant

    @OneToOne
    private PromoEntity promo; //one order can have one promo at max
    private Boolean promoApplied = false;
}
