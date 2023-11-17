package org.binaracademy.challenge_7.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private String destination;
    private LocalDate time;
    private Boolean isCompleted;
    private List<OrderDetailResponse> orderDetailResponses;

    public OrderResponse(Long orderId, Long userId, String destination, LocalDate time, Boolean isCompleted){
        this.orderId = orderId;
        this.userId = userId;
        this.destination = destination;
        this.time = time;
        this.isCompleted = isCompleted;
    }
}
