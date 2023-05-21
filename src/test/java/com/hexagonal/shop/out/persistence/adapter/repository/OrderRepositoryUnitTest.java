package com.hexagonal.shop.out.persistence.adapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hexagonal.shop.adapter.out.persistence.entity.CategoryEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.MemberEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderEntity;
import com.hexagonal.shop.adapter.out.persistence.entity.OrderItemEntity;
import com.hexagonal.shop.adapter.out.persistence.repository.CategoryRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.ItemRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.MemberRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.OrderItemRepository;
import com.hexagonal.shop.adapter.out.persistence.repository.OrderRepository;
import com.hexagonal.shop.common.ShopConstants.OrderState;

public class OrderRepositoryUnitTest extends BaseRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private void insertInitData() throws Exception {

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

        // member 초기 data insert
        MemberEntity memberEntity = MemberEntity.builder()
                            .id("JJM")
                            .address("수원")
                            .phoneNumber("01111111111")
                            .build();

        memberRepository.save(memberEntity);
        memberRepository.flush();

        // order 초기 data insert
        OrderEntity orderEntity = OrderEntity.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .orderState(OrderState.REQUEST)
                        .build();
        
        orderEntity.setMemberEntity(memberEntity);
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
                        .categoryEntity(categoryEntity)
                        .build();

        ItemEntity itemEntity2 = ItemEntity.builder()
                        .name("Y-shirt")
                        .price(300)
                        .remainQty(500)
                        .categoryEntity(categoryEntity)
                        .build();

        itemEntity1.setCategoryEntity(categoryEntity);
        itemEntity2.setCategoryEntity(categoryEntity);

        itemRepository.save(itemEntity1);
        itemRepository.save(itemEntity2);

        itemRepository.flush();

        // order 
        // T-shirt 3개 주문 
        // Y-shirt 2개 주문 
        OrderItemEntity orderitemEntity1 = OrderItemEntity.builder()
                                .itemEntity(itemEntity1)
                                .count(3)
                                .build();

        OrderItemEntity orderitemEntity2 = OrderItemEntity.builder()
                                .itemEntity(itemEntity2)
                                .count(2)
                                .build();

        orderitemEntity1.setOrderEntity(orderEntity);
        orderitemEntity2.setOrderEntity(orderEntity);

        orderItemRepository.save(orderitemEntity1);
        orderItemRepository.save(orderitemEntity2);

        orderItemRepository.flush();
    }

    @Test
    public void order_test() throws Exception {
        
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");

        // order 초기 data insert
        OrderEntity orderEntity1 = OrderEntity.builder()
                        .orderDate(dtFormat.parse("20230416"))
                        .orderState(OrderState.REQUEST)
                        .build();

        OrderEntity orderEntity2 = OrderEntity.builder()
                        .orderDate(dtFormat.parse("20230417"))
                        .orderState(OrderState.REQUEST)
                        .build();

        orderRepository.save(orderEntity1);
        orderRepository.save(orderEntity2);

        orderRepository.flush();

        // 전체 조회 테스트
        List<OrderEntity> orderEntities = orderRepository.findAll();
        assertEquals(2, orderEntities.size());

        // orderDate 로 조회 테스트 LessThan
        orderEntities = orderRepository.findByOrderDateLessThan(dtFormat.parse("20230418"));
        assertEquals(2, orderEntities.size());

        // orderDate 로 조회 테스트 LessThan
        orderEntities = orderRepository.findByOrderDateLessThan(dtFormat.parse("20230417"));
        assertEquals(1, orderEntities.size());

        // orderDate 로 조회 테스트 GreaterThan
        orderEntities = orderRepository.findByOrderDateGreaterThan(dtFormat.parse("20230418"));
        assertEquals(0, orderEntities.size());

        // orderDate 로 조회 테스트 GreaterThan
        orderEntities = orderRepository.findByOrderDateGreaterThan(dtFormat.parse("20230415"));
        assertEquals(2, orderEntities.size());

        // orderDate 로 조회 테스트 between
        orderEntities = orderRepository.findByOrderDateBetween(dtFormat.parse("20230415"), dtFormat.parse("20230419"));
        assertEquals(2, orderEntities.size());

        // orderDate 로 조회 테스트 between
        orderEntities = orderRepository.findByOrderDateBetween(dtFormat.parse("20230415"), dtFormat.parse("20230416"));
        assertEquals(1, orderEntities.size());

    }

    @Test
    public void findByOrderId_test() throws Exception{
        
        insertInitData();

        OrderEntity returnOrder = orderRepository.findOrderInfoByOrderId(1L);
        assertEquals(2, returnOrder.getOrderItemEntities().size());

    }

}
