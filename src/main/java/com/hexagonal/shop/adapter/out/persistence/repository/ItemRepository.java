package com.hexagonal.shop.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;

import jakarta.persistence.LockModeType;

public interface ItemRepository extends JpaRepository<ItemEntity, Long>{
    
    public List<ItemEntity> findByName(String name);

    @Query(value = "SELECT i from ItemEntity i join fetch i.categoryEntity c WHERE c.id = :categoryId")
    public List<ItemEntity> findByCategoryId(Long categoryId);

    public List<ItemEntity> findByPriceGreaterThan(int price);

    public List<ItemEntity> findByRemainQtyGreaterThan(int remainQty);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "SELECT i from ItemEntity i WHERE i.id = :id")
    public ItemEntity findByIdForUpdate(@Param("id") Long id);

}
