package com.hexagonal.shop.out.persistence.adapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hexagonal.shop.adapter.out.persistence.entity.CategoryEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;
import com.hexagonal.shop.adapter.out.persistence.repository.CategoryRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.ItemRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.OrderItemRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.OrderRepository;
import com.hexagonal.shop.common.ShopConstants.OrderState;



public class OrderItemRepositoryUnitTest extends BaseRepositoryTest{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void orderItem_test() throws Exception{

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        
        // order 초기 data insert
        OrderEntity orderEntity = OrderEntity.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .orderState(OrderState.REQUEST)
                        .build();
        
        orderRepository.save(orderEntity);
        orderRepository.flush();

        CategoryEntity categoryEntity = CategoryEntity.builder()
                                    .name("MEN")
                                    .build();
        categoryRepository.save(categoryEntity);
        categoryRepository.flush();

        // item 초기 data insert
        ItemEntity itemEntity1 = ItemEntity.builder()
                        .name("T-shirt")
                        .price(500)
                        .remainQty(500)
                        .build();

        ItemEntity itemEntity2 = ItemEntity.builder()
                        .name("Y-shirt")
                        .price(300)
                        .remainQty(500)
                        .build();

        itemEntity1.setCategoryEntity(categoryEntity);
        itemEntity2.setCategoryEntity(categoryEntity);
        
        itemRepository.save(itemEntity1);
        itemRepository.save(itemEntity2);

        itemRepository.flush();

        // order 
        // T-shirt 3개 주문 
        // Y-shirt 2개 주문 
        OrderItemEntity orderItemEntity1 = OrderItemEntity.builder()
                                .itemEntity(itemEntity1)
                                .count(3)
                                .build();

        OrderItemEntity orderItemEntity2 = OrderItemEntity.builder()
                                .itemEntity(itemEntity2)
                                .count(2)
                                .build();
        
        orderItemEntity1.setOrderEntity(orderEntity);
        orderItemEntity2.setOrderEntity(orderEntity);
              
        orderItemRepository.save(orderItemEntity1);
        orderItemRepository.save(orderItemEntity2);

        orderItemRepository.flush();

        List<OrderItemEntity> orderItemEntities = orderItemRepository.findAll();
        assertEquals(2, orderItemEntities.size());

        orderItemEntities = orderItemRepository.findByOrderId(orderEntity.getId());
        assertEquals(2, orderItemEntities.size());

    }
    
}
