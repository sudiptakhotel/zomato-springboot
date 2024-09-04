package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = "menuItems")
@EqualsAndHashCode(exclude = "menuItems")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private Boolean isOpen;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Boolean isVeg;
    private Boolean isRecommendedForChild;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    private Point restaurantLocation;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<MenuItemEntity> menuItems;

    @OneToOne
    private UserWalletEntity wallet;

    private Double rating = 0.0;
}
