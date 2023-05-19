package com.hexagonal.shop.adapter.out.persistence.repository;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hexagonal.shop.adapter.out.persistence.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity>{
    
    public List<OrderEntity> findByOrderDateLessThan(Date orderDate);

    public List<OrderEntity> findByOrderDateGreaterThan(Date orderDate);

    public List<OrderEntity> findByOrderDateBetween(Date start, Date end);

    @Query(value = "SELECT o from OrderEntity o join fetch o.memberEntity m join fetch o.orderItemEntities oi join fetch oi.itemEntity i  join fetch i.categoryEntity WHERE o.id = :orderId")
    OrderEntity findOrderInfoByOrderId(Long orderId);

    @Query(value = "SELECT o from OrderEntity o join fetch o.memberEntity m join fetch o.orderItemEntities oi join fetch oi.itemEntity i  join fetch i.categoryEntity WHERE m.id = :memberId")
    List<OrderEntity> findOrderInfoByMemberId(String memberId);

}
