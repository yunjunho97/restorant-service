package com.example.weekly0719.service;

import com.example.weekly0719.model.*;
import com.example.weekly0719.repository.CustomerRepository;
import com.example.weekly0719.repository.MenuRepository;
import com.example.weekly0719.repository.OrderItemRepository;
import com.example.weekly0719.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MenuService menuService;
    private final CustomerService customerService;

    @Autowired
    public OrderService(OrderRepository orderRepository, MenuService menuService, CustomerService customerService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.customerService = customerService;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderDTO> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToOrderEntity(orderDTO);
//        Customer customer = customerService.getCustomerById(orderDTO.getCustomerId())
//                .map(CustomerDTO::toEntity)
//                .orElseThrow(EntityNotFoundException::new);
//        customer.addOrder(order);
        return convertToOrderDTO(orderRepository.save(order));
    }

    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToOrderDTO);
    }

    @Transactional
    public Optional<OrderDTO> updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.getOrderItems().clear();
        order.setTotalPrice(0L);
        for(OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            OrderItem orderItem = convertToOrderItemEntity(orderItemDTO);
            order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
            order.addOrderItem(orderItem);
        }
        orderRepository.save(order);
        return Optional.of(convertToOrderDTO(order));
    }

    public Optional<OrderDTO> updateStateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.setStatus(orderDTO.getStatus());
        orderRepository.save(order);
        return Optional.of(convertToOrderDTO(order));
    }

    public void deleteOrder(Long orderId) {
        //
        orderRepository.deleteById(orderId);
    }

    private Order convertToOrderEntity(OrderDTO orderDTO){
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setStatus(orderDTO.getStatus());
        order.setTotalPrice(0L);
        if(orderDTO.getOrderItems() != null){
            orderDTO.getOrderItems().forEach(orderItemDTO -> {
                OrderItem orderItem = this.convertToOrderItemEntity(orderItemDTO);
                order.setTotalPrice(order.getTotalPrice() + orderItem.getPrice());
                order.addOrderItem(orderItem);
            });
        }
//        Customer customer = customerService.getCustomerById(orderDTO.getCustomerId())
//                .map(CustomerDTO::toEntity)
//                .orElseThrow(EntityNotFoundException::new);
//        customer.addOrder(order);
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
