package com.hexagonal.shop.out.persistence.adapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hexagonal.shop.adapter.out.persistence.entity.CategoryEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.adapter.out.persistence.repository.CategoryRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.ItemRepository;


public class ItemRepositoryUnitTest  extends BaseRepositoryTest {
       
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void CategoryEntity_test() {

        // CategoryEntity 초기 값 insert
        CategoryEntity categoryEntity1 = CategoryEntity.builder()
                                .name("WOMEN")
                                .build();

        
        CategoryEntity categoryEntity2 = CategoryEntity.builder()
                                .name("MEN")
                                .build();     

        categoryRepository.save(categoryEntity1);
        categoryRepository.save(categoryEntity2);

        categoryRepository.flush();
        
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

        ItemEntity itemEntity3 = ItemEntity.builder()
                        .name("men's T dress")
                        .price(55000)
                        .remainQty(50)
                        .build();  


        // ItemEntity의 CategoryEntity 지정
        itemEntity1.setCategoryEntity(categoryEntity1);
        itemEntity2.setCategoryEntity(categoryEntity1);
        itemEntity3.setCategoryEntity(categoryEntity2);
        
        // ItemEntity 저장
        itemRepository.save(itemEntity1);
        itemRepository.save(itemEntity2);
        itemRepository.save(itemEntity3);

        itemRepository.flush();

        // women's T shirt ItemEntity 조회
        List<ItemEntity> itemEntities = itemRepository.findByName("women's T shirt");

        // 조회 확인
        assertEquals(1, itemEntities.size());
        assertEquals("WOMEN", itemEntities.get(0).getCategoryEntity().getName());
        
        // WOMEN CategoryEntity id로 ItemEntity 조회 
        itemEntities = itemRepository.findByCategoryId(categoryEntity1.getId());
        assertEquals(2, itemEntities.size());

        // 45000 원 보다 비싼 ItemEntity  조회
        itemEntities = itemRepository.findByPriceGreaterThan(45000);
        assertEquals(2, itemEntities.size());

        // 4500 원 보다 비싼 ItemEntity  조회
        itemEntities = itemRepository.findByPriceGreaterThan(4500);
        assertEquals(3, itemEntities.size());

        // 재고가 0개 보다 많은 ItemEntity 조회 
        itemEntities = itemRepository.findByRemainQtyGreaterThan(0);
        assertEquals(2, itemEntities.size());

        // qty update test 
        itemEntity2.updateItem(itemEntity2.getName(), itemEntity2.getPrice(), 10000);
        itemRepository.flush();
        assertEquals(10000,itemRepository.findById(itemEntity2.getId()).orElse(null).getRemainQty());
        
    }

    @Test
    public void ItemEntityUpdate_test() {
        
        // 초기 ItemEntity data insert
        ItemEntity itemEntity = ItemEntity.builder()
                        .name("T shirt")
                        .price(5000)
                        .remainQty(0)
                        .build();

        itemRepository.save(itemEntity);
        itemRepository.flush();

        // select for update 
        /* Hibernate: 
        select
            i1_0.ItemEntity_ID,
            i1_0.CategoryEntity_ID,
            i1_0.name,
            i1_0.price,
            i1_0.remain_qty 
        from
            tb_shop_ItemEntity i1_0 
        where
            i1_0.ItemEntity_ID=? for update */
        ItemEntity updateItemEntity = itemRepository.findByIdForUpdate(itemEntity.getId());

        // 가격 변경
        updateItemEntity.updateItem(updateItemEntity.getName(), 500, updateItemEntity.getRemainQty());;
        itemRepository.flush();

        itemEntity = itemRepository.findById(itemEntity.getId()).orElse(null);

        assertEquals(500, itemEntity.getPrice());

    }
}
