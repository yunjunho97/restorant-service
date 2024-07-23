package com.example.weekly0719.service;

import com.example.weekly0719.model.*;
import com.example.weekly0719.repository.CustomerRepository;
import com.example.weekly0719.repository.MenuRepository;
import com.example.weekly0719.repository.OrderItemRepository;
import com.example.weekly0719.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuService menuService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
    }

    public List<OrderDTO> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToOrderEntity(orderDTO);
        return convertToOrderDTO(orderRepository.save(order));
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    private Order convertToOrderEntity(OrderDTO orderDTO){
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setStatus(orderDTO.getStatus());
        order.setTotalPrice(orderDTO.getTotalPrice());
        if(orderDTO.getOrderItems() != null){
            orderDTO.getOrderItems().forEach(orderItemDTO -> {
                OrderItem orderItem = this.convertToOrderItemEntity(orderItemDTO);
                order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
                order.addOrderItem(orderItem);
            });
        }
        return order;
    }

    private OrderDTO convertToOrderDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTotalPrice(order.getTotalPrice());
        if(order.getOrderItems() != null){
            orderDTO.setOrderItems(
                    order.getOrderItems().stream()
                    .map(this::convertToOrderItemDTO)
                    .collect(Collectors.toList())
            );
        }
        return orderDTO;
    }

    private OrderItem convertToOrderItemEntity(OrderItemDTO orderItemDTO){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.getId());
        Optional<Menu> menu = menuService.getById(orderItemDTO.getMenuId()).map(MenuDTO::toEntity);
        orderItem.setPrice(menu.get().getPrice());
        orderItem.setMenu(menu.get());
        return orderItem;
    }

    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem){
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setMenuId(orderItem.getMenu().getId());
        return orderItemDTO;
    }
}
