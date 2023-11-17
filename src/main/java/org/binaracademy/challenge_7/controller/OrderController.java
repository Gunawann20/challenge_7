package org.binaracademy.challenge_7.controller;

import org.binaracademy.challenge_7.entity.response.OrderResponse;
import org.binaracademy.challenge_7.entity.response.Response;
import org.binaracademy.challenge_7.service.OrderService;
import org.binaracademy.challenge_7.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OrderController {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService){
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @PostMapping(
            value = "/order/{productCode}/{quantity}"
    )
    public ResponseEntity<Response<String>> createOrder(@PathVariable Long productCode, @PathVariable Integer quantity){
        return ResponseEntity.ok(orderService.createOrder(productCode, quantity));
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping(
            value = "/order",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<OrderResponse>> activeOrder(){
        return ResponseEntity.ok(orderService.currentOrder());
    }

    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping(
            value = "/order/history",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<OrderResponse>>> historyOrder(){
        return ResponseEntity.ok(orderService.historyOrder());
    }
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @GetMapping(
            value = "/order/print-invoice",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public byte[] printInvoice(){
        return orderService.makeOrder();
    }
}
