package com.majorproject.zomato.ZomatoApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_item" ,
indexes = @Index(name = "idx_menu_item_restaurant" , columnList = "restaurant_id")
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private RestaurantEntity restaurant;

}
