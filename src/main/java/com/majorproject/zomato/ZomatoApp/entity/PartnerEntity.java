package com.majorproject.zomato.ZomatoApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "partner")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PartnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    private Boolean available;

    @OneToMany
    @JoinTable(name = "partner_order_mapping" ,
            joinColumns = @JoinColumn(name = "partner_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )

    private List<OrderEntity> orders;

    @Column(columnDefinition = "Geometry(Point , 4326)")
    private Point currentLocation;

    private Double rating = 0.0;

}
