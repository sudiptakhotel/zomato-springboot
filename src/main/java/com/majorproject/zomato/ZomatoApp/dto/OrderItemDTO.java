
package com.majorproject.zomato.ZomatoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long id;
    private String itemName;
    private Double itemPrice;
    private Integer quantity;
}
