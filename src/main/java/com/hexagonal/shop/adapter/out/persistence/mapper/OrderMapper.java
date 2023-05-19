package com.hexagonal.shop.adapter.out.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hexagonal.shop.adapter.out.persistence.entity.MemberEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;
import com.hexagonal.shop.domain.Member;
import com.hexagonal.shop.domain.Order;
import com.hexagonal.shop.domain.OrderItem;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderMapper implements DomainEntityMapper<Order, OrderEntity> {

    private final DomainEntityMapper<Member, MemberEntity> memberMapper;
    
    private final DomainEntityMapper<OrderItem, OrderItemEntity> orderItemMapper;

    public List<OrderEntity> domainToEntity(List<Order> orders) {
        List<OrderEntity> orderEntities = new ArrayList<OrderEntity>();
        for(Order o : orders) {
            orderEntities.add(domainToEntity(o));
        }
        return orderEntities;
    }
    
    public OrderEntity domainToEntity(Order order) {
        if (order == null ){
            return null;
        }
        return OrderEntity.builder()
                            .id(order.getId())
                            .memberEntity(memberMapper.domainToEntity(order.getMember()))
                            .orderItemEntities(orderItemMapper.domainToEntity(order.getOrderItems()))
                            .orderDate(order.getOrderDate())
                            .orderState(order.getOrderState())
                            .build();
                            
    }

    public List<Order> entityToDomain(List<OrderEntity> orderEntities) {
        List<Order> orders = new ArrayList<Order>();
        for(OrderEntity oe : orderEntities) {
            orders.add(entityToDomain(oe));
        }
        return orders;                   
    }

    public Order entityToDomain(OrderEntity orderEntity) {
        if (orderEntity == null ){
            return null;
        }
        return Order.builder()
                    .id(orderEntity.getId())
                    .member(memberMapper.entityToDomain(orderEntity.getMemberEntity()))
                    .orderItems(orderItemMapper.entityToDomain(orderEntity.getOrderItemEntities()))
                    .orderDate(orderEntity.getOrderDate())
                    .orderState(orderEntity.getOrderState())
                    .build();
    }

    
}
