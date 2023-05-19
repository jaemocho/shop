package com.hexagonal.shop.application.port.out;

import java.util.List;

import com.hexagonal.shop.domain.Category;

public interface CategoryPersistencePort {
    public Category save(Category category);

    public List<Category> findAll();

    public List<Category> findByName(String name);

    public Category findById(Long id);

    public void delete(Category category);
}
