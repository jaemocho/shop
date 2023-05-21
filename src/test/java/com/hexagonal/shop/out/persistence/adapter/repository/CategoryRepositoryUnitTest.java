package com.hexagonal.shop.out.persistence.adapter.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hexagonal.shop.adapter.out.persistence.entity.CategoryEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.adapter.out.persistence.repository.CategoryRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.ItemRepository;

public class CategoryRepositoryUnitTest extends BaseRepositoryTest{
    
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void CategoryEntity_test() {
        
        // CategoryEntity 초기 date insert
        CategoryEntity categoryEntity1 = CategoryEntity.builder()
                                .name("WOMEN")
                                .build();

        
        CategoryEntity categoryEntity2 = CategoryEntity.builder()
                                .name("MEN")
                                .build();        

        CategoryEntity categoryEntity3 = CategoryEntity.builder()
                                .name("KIDS")
                                .build();

        CategoryEntity categoryEntity4 = CategoryEntity.builder()
                                .name("BABY")
                                .build();                                
        

        categoryRepository.save(categoryEntity1);
        categoryRepository.save(categoryEntity2);
        categoryRepository.save(categoryEntity3);
        categoryRepository.save(categoryEntity4);

        categoryRepository.flush();

        // 전체 조회 테스트 
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        assertEquals(4, categoryEntities.size());

        
        // 삭제 테스트 
        categoryRepository.delete(categoryEntity2);
        categoryEntities = categoryRepository.findAll();
        assertEquals(3, categoryEntities.size());

        // CategoryEntity 에 ItemEntity 추가 후 조회 테스트 
        categoryEntities = categoryRepository.findByName("WOMEN");
        CategoryEntity womenCategoryEntity = categoryEntities.get(0);

        // ItemEntity insert
        ItemEntity itemEntity1 = ItemEntity.builder()
                        .name("women's T shirt")
                        .price(5000)
                        .remainQty(0)
                        .build();

        ItemEntity itemEntity2 = ItemEntity.builder()
                        .name("women's T dress")
                        .price(50000)
                        .remainQty(50)
                        .build();                        
        
        // ItemEntity 저장
        itemRepository.save(itemEntity1);
        itemRepository.save(itemEntity2);

        itemRepository.flush();

        // CategoryEntity 에 ItemEntity 추가 
        womenCategoryEntity.addItemEntity(itemEntity1);
        womenCategoryEntity.addItemEntity(itemEntity2);
        
        categoryRepository.flush();

        categoryEntities = categoryRepository.findByName("WOMEN");
        womenCategoryEntity = categoryEntities.get(0);
        assertEquals(2, womenCategoryEntity.getItemEntities().size());
    }

}
