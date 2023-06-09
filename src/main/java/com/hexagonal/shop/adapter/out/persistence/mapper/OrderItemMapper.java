package com.hexagonal.shop.adapter.out.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;
import com.hexagonal.shop.domain.Item;
import com.hexagonal.shop.domain.OrderItem;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemMapper implements DomainEntityMapper<OrderItem,OrderItemEntity> {

    private final DomainEntityMapper<Item,ItemEntity> itemMapper;

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
                            .itemEntity(itemMapper.domainToEntity(orderItem.getItem()))
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
                    .item(itemMapper.entityToDomain(orderItemEntity.getItemEntity()))
                    .count(orderItemEntity.getCount())
                    .build();                       
    }
}
