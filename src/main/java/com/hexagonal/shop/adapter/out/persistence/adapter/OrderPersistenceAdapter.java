package com.hexagonal.shop.adapter.out.persistence.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hexagonal.shop.adapter.out.persistence.entity.OrderEntity;
import com.hexagonal.shop.adapter.out.persistence.mapper.DomainEntityMapper;
import com.hexagonal.shop.adapter.out.persistence.repository.OrderRepository;
import com.hexagonal.shop.application.port.out.OrderPersistencePort;
import com.hexagonal.shop.domain.Order;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderPersistenceAdapter implements OrderPersistencePort {
    
    private final OrderRepository orderRepository;

    private final DomainEntityMapper<Order, OrderEntity> orderMapper;
    

    public Order save(Order order) {
        OrderEntity orderEntity = domainToEntity(order);
        orderEntity = orderRepository.save(orderEntity);
        return entityToDomain(orderEntity);
    }

    public List<Order> findAll() {
        return entityToDomain(orderRepository.findAll());
    }

    public Order findById(Long id) {
        return entityToDomain(orderRepository.findById(id).orElse(null));
    }

    public Order findOrderInfoByOrderId(Long id) {
        return entityToDomain(orderRepository.findOrderInfoByOrderId(id));
    }

    public List<Order> findOrderInfoByMemberId(String id) {
        return entityToDomain(orderRepository.findOrderInfoByMemberId(id));
    }

    public List<Order> findByOrderDateLessThan(Date orderDate) {
        return entityToDomain(orderRepository.findByOrderDateLessThan(orderDate));
    }

    public List<Order> findByOrderDateGreaterThan(Date orderDate) {
        return entityToDomain(orderRepository.findByOrderDateGreaterThan(orderDate));
    }

    public List<Order> findByOrderDateBetween(Date start, Date end) {
        return entityToDomain(orderRepository.findByOrderDateBetween(start, end));
    }

    public void delete(Order order) {
        OrderEntity orderEntity = domainToEntity(order);
        orderRepository.delete(orderEntity);
    }

    // public List<Order> findOrders(OrderSearch orderSearch) {
    //     return orderRepository.findAll(orderSearch.toSpecification());
    // }

    private Order entityToDomain(OrderEntity orderEntity) {
        return orderMapper.entityToDomain(orderEntity);
    }

    private List<Order> entityToDomain(List<OrderEntity> orderEntities) {
        return orderMapper.entityToDomain(orderEntities);
    }

    private OrderEntity domainToEntity(Order order) {
        return orderMapper.domainToEntity(order);
    }
}
