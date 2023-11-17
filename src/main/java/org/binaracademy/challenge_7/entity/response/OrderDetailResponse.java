package org.binaracademy.challenge_7.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {

    private Long orderId;
    private String productName;
    private Integer quantity;
    private Long totalPrice;

}
