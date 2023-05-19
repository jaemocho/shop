package com.hexagonal.shop.adapter.out.persistence.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long>{
    
    @Query(value = "SELECT oi from OrderItemEntity oi join fetch oi.orderEntity o join fetch oi.itemEntity i join fetch i.categoryEntity WHERE o.id = :orderId")
    List<OrderItemEntity> findByOrderId(Long orderId);
}
