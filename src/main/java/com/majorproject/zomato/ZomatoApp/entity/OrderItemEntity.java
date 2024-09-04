package com.majorproject.zomato.ZomatoApp.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item" ,
indexes = {
        @Index(name = "idx_order_item_order_request" , columnList = "order_request_id")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private OrderRequestEntity orderRequest;

    @ManyToOne(cascade = CascadeType.ALL)
    private CartEntity cart;

    @ManyToOne
    private MenuItemEntity menuItem;

    private String itemName;
    private Double itemPrice;
    private Integer quantity;


}
