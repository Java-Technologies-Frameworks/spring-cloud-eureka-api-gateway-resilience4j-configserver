package com.jtf.orderservice.api.controller;

import com.jtf.orderservice.api.common.TransactionRequest;
import com.jtf.orderservice.api.common.TransactionResponse;
import com.jtf.orderservice.api.entity.Order;
import com.jtf.orderservice.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

//    @PostMapping("/bookOrder")
//    public Order bookOrder(@RequestBody Order order) {
//        return orderService.saveOrder(order);
//    }

    @PostMapping("/bookOrder")
    public TransactionResponse bookOrder(@RequestBody TransactionRequest request) {
        return orderService.saveOrder(request);
    }
}
