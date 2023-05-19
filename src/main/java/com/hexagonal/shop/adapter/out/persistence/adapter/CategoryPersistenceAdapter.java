package com.hexagonal.shop.adapter.out.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexagonal.shop.adapter.out.persistence.entity.CategoryEntity;
import com.hexagonal.shop.adapter.out.persistence.mapper.DomainEntityMapper;
import com.hexagonal.shop.adapter.out.persistence.repository.CategoryRepository;
import com.hexagonal.shop.application.port.out.CategoryPersistencePort;
import com.hexagonal.shop.domain.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPersistencePort{
    
    private final CategoryRepository categoryRepository;
    
    private final DomainEntityMapper<Category, CategoryEntity> categoryMapper;

    public Category save(Category category){
        CategoryEntity categoryEntity = domainToEntity(category);
        categoryEntity = categoryRepository.save(categoryEntity);
        return entityToDomain(categoryEntity);
    }

    public List<Category> findAll() {
        return entityToDomain(categoryRepository.findAll());
    }

    public List<Category> findByName(String name) {
        return entityToDomain(categoryRepository.findByName(name));
    }

    public Category findById(Long id) {
        return entityToDomain(categoryRepository.findById(id).orElse(null));
    }

    public void delete(Category category) {
        CategoryEntity categoryEntity = domainToEntity(category);
        categoryRepository.delete(categoryEntity);
    }

    private Category entityToDomain(CategoryEntity categoryEntity) {
        return categoryMapper.entityToDomain(categoryEntity);
    }

    private List<Category> entityToDomain(List<CategoryEntity> categoryEntity) {
        return categoryMapper.entityToDomain(categoryEntity);
    }

    private CategoryEntity domainToEntity(Category category) {
        return categoryMapper.domainToEntity(category);
    }
}
