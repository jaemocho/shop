package com.hexagonal.shop.adapter.out.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hexagonal.shop.adapter.out.persistence.entity.OrderEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;
import com.hexagonal.shop.domain.Order;
import com.hexagonal.shop.domain.OrderItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemMapper implements DomainEntityMapper<OrderItem,OrderItemEntity> {

    private final ItemMapper imtemMapper;

    public List<OrderItemEntity> domainToEntity(List<OrderItem> orderItems) {
        List<OrderItemEntity> orderItemEntities = new ArrayList<OrderItemEntity>();
        for(OrderItem oi : orderItems) {
            orderItemEntities.add(domainToEntity(oi));
        }
        return orderItemEntities;                   
    }
    
    public OrderItemEntity domainToEntity(OrderItem orderItem) {
        if (orderItem == null ){
            return null;
        }
        return OrderItemEntity.builder()
                            .id(orderItem.getId())
                            .itemEntity(imtemMapper.domainToEntity(orderItem.getItem()))
                            .orderEntity(createNewOrderEntity(orderItem))
                            .count(orderItem.getCount())
                            .build();
    }

    public List<OrderItem> entityToDomain(List<OrderItemEntity> orderItemEntities) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for(OrderItemEntity oie : orderItemEntities) {
            orderItems.add(entityToDomain(oie));
        }
        return orderItems;                   
    }

    public OrderItem entityToDomain(OrderItemEntity orderItemEntity) {
        if (orderItemEntity == null ){
            return null;
        }
        return OrderItem.builder()
                    .id(orderItemEntity.getId())
                    .item(imtemMapper.entityToDomain(orderItemEntity.getItemEntity()))
                    .order(createNewOrder(orderItemEntity))
                    .count(orderItemEntity.getCount())
                    .build();
                            
    }

    private Order createNewOrder(OrderItemEntity orderItemEntity) {
        Long orderId = orderItemEntity.getOrderEntity().getId();
        if (orderId == null) return null;
        return Order.builder()
                    .id(orderId)
                    .build();
    }

    private OrderEntity createNewOrderEntity(OrderItem orderItem) {
        Long orderId = orderItem.getOrder().getId();
        if(orderId == null) return null;
        return OrderEntity.builder()
                        .id(orderId)
                        .build();
    }
}
