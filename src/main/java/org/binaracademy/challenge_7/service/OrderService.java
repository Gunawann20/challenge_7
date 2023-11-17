package org.binaracademy.challenge_7.service;

import org.binaracademy.challenge_7.entity.response.OrderResponse;
import org.binaracademy.challenge_7.entity.response.Response;

import java.util.List;

public interface OrderService {
    Response<String> createOrder(Long productCode, Integer quantity);
    Response<OrderResponse> currentOrder();
    Response<List<OrderResponse>> historyOrder();
    byte[] makeOrder();

}
