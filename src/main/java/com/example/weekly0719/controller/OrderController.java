package com.example.weekly0719.controller;

import com.example.weekly0719.model.OrderDTO;
import com.example.weekly0719.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrder() {
        List<OrderDTO> orders = orderService.getAllOrder();
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(createdOrderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/{orderId}/order-items")
//    public ResponseEntity<OrderItemDTO> createOrderItem(@PathVariable Long orderId, @RequestBody OrderItemDTO createOrderItemDTO) {
//        OrderItemDTO createdOrderItemDTO = orderService.create
//    }
}
