package com.hexagonal.shop.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexagonal.shop.adapter.out.persistence.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

    public List<CategoryEntity> findByName(String name);

}