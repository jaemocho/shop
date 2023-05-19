package com.hexagonal.shop.adapter.out.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;
import com.hexagonal.shop.adapter.out.persistence.mapper.DomainEntityMapper;
import com.hexagonal.shop.adapter.out.persistence.repository.OrderItemRepository;
import com.hexagonal.shop.application.port.out.OrderItemPersistencePort;
import com.hexagonal.shop.domain.OrderItem;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderItemPersistenceAdapter implements OrderItemPersistencePort{

    private final OrderItemRepository orderItemRepository;

    private final DomainEntityMapper<OrderItem, OrderItemEntity> orderItemMapper;

    public OrderItem save(OrderItem orderItem) {
        OrderItemEntity orderItemEntity = domainToEntity(orderItem);
        orderItemEntity = orderItemRepository.save(orderItemEntity);
        return entityToDomain(orderItemEntity);
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return entityToDomain(orderItemRepository.findByOrderId(orderId));
    }

    public void delete(OrderItem orderItem) {
        OrderItemEntity orderItemEntity = domainToEntity(orderItem);
        orderItemRepository.delete(orderItemEntity);
    }

    private OrderItem entityToDomain(OrderItemEntity orderItemEntity) {
        return orderItemMapper.entityToDomain(orderItemEntity);
    }

    private List<OrderItem> entityToDomain(List<OrderItemEntity> orderItemEntities) {
        return orderItemMapper.entityToDomain(orderItemEntities);
    }

    private OrderItemEntity domainToEntity(OrderItem orderItem) {
        return orderItemMapper.domainToEntity(orderItem);
    }
}
