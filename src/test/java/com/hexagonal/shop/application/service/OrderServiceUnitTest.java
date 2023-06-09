package com.hexagonal.shop.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hexagonal.shop.application.port.out.ItemPersistencePort;
import com.hexagonal.shop.application.port.out.OrderItemPersistencePort;
import com.hexagonal.shop.application.port.out.OrderPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqOrderDto;
import com.hexagonal.shop.application.service.dto.RespOrderDto;
import com.hexagonal.shop.common.ShopConstants.OrderState;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Category;
import com.hexagonal.shop.domain.Item;
import com.hexagonal.shop.domain.Member;
import com.hexagonal.shop.domain.Order;
import com.hexagonal.shop.domain.OrderItem;


@ExtendWith(SpringExtension.class)
public class OrderServiceUnitTest {
    
    @Mock
    private OrderPersistencePort orderPersistencePort;
    
    @Mock
    private OrderItemPersistencePort orderItemPersistencePort;

    @Mock
    private ItemPersistencePort itemPersistencePort;

    @Mock
    private MemberService memberService;

    @Mock 
    private ItemService itemService;


    @InjectMocks
    private OrderService orderService;


    private List<Category> initCategoryData() {

        List<Category> categorys = new ArrayList<Category>();
        Category category1 = Category.builder().id(1L).name("WOMEN").build();
        Category category2 = Category.builder().id(2L).name("MEN").build();
        Category category3 = Category.builder().id(3L).name("KIDS").build();
        categorys.add(category1);
        categorys.add(category2);
        categorys.add(category3);

        return categorys;
    }

    private List<Member> initMemberData() {

        List<Member> members = new ArrayList<Member>();
        Member member1 = Member.builder().id("member1").address("수원").phoneNumber("0101112222").build();
        Member member2 = Member.builder().id("member2").address("서울").phoneNumber("0101113333").build();        
        Member member3 = Member.builder().id("member3").address("대전").phoneNumber("0101114444").build();                                
        members.add(member1);
        members.add(member2);
        members.add(member3);
        return members;
    }


    private List<Item> initItemData() {

        List<Item> items = new ArrayList<Item>();
        Item item1 = Item.builder().id(1L).name("T-shirt").price(5000).remainQty(100).build();
        Item item2 = Item.builder().id(2L).name("T-shirt").price(4500).remainQty(100).build();
        Item item3 = Item.builder().id(3L).name("T-shirt").price(50000).remainQty(100).build();
        items.add(item1);
        items.add(item2);
        items.add(item3);
        return items;
    }

    @Test
    public void createOrder_test() throws Exception{

        //given
        List<Member> members = initMemberData();
        List<Item> items = initItemData();
        // /List<Category> categorys = initCategoryData();

        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();
        order.setMember(members.get(0));
        
        List<ReqOrderDto.RequestItem> requestItems = new ArrayList<ReqOrderDto.RequestItem>();

        requestItems.add(ReqOrderDto.RequestItem.builder()
                                        .itemId(items.get(0).getId())
                                        .requestQty(30)
                                        .build());

        requestItems.add(ReqOrderDto.RequestItem.builder()
                                        .itemId(items.get(1).getId())
                                        .requestQty(15)
                                        .build());                                      

        
        ReqOrderDto reqOrderDto = ReqOrderDto.builder()
                                        .memberId(members.get(0).getId())
                                        .requestItem(requestItems)
                                        .build();
        
        when(memberService.getMember(reqOrderDto.getMemberId())).thenReturn(members.get(0));
        when(itemService.getItemForUpdate(items.get(0).getId())).thenReturn(items.get(0));
        when(itemService.getItemForUpdate(items.get(1).getId())).thenReturn(items.get(1));

        when(orderPersistencePort.save(any(Order.class))).thenReturn(order);
        when(orderItemPersistencePort.save(any(OrderItem.class))).thenReturn(null);
        

        // when
        orderService.createOrder(reqOrderDto);

        // then 
        assertEquals(70, items.get(0).getRemainQty());
        assertEquals(85, items.get(1).getRemainQty());

    }

    @Test   // reamin qty 보다 많이 요청했을 경우 exception 테스트
    public void createOrderNotEnough_test() throws Exception{

        //given
        List<Member> members = initMemberData();
        List<Item> items = initItemData();
        // /List<Category> categorys = initCategoryData();

        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();
        order.setMember(members.get(0));
        
        List<ReqOrderDto.RequestItem> requestItems = new ArrayList<ReqOrderDto.RequestItem>();

        requestItems.add(ReqOrderDto.RequestItem.builder()
                                        .itemId(items.get(0).getId())
                                        .requestQty(101)
                                        .build());
        
        ReqOrderDto reqOrderDto = ReqOrderDto.builder()
                                        .memberId(members.get(0).getId())
                                        .requestItem(requestItems)
                                        .build();
        
        when(memberService.getMember(reqOrderDto.getMemberId())).thenReturn(members.get(0));
        when(itemService.getItemForUpdate(items.get(0).getId())).thenReturn(items.get(0));

        when(orderPersistencePort.save(any(Order.class))).thenReturn(order);
        when(orderItemPersistencePort.save(any(OrderItem.class))).thenReturn(null);
        

        // when then
        assertThrows(ShopException.class, ()->orderService.createOrder(reqOrderDto));
    }

    @Test   // member 정보가 잘못된 경우 exception 발생 테스트
    public void createOrderNotFoundMember_test() throws Exception{

        //given
        List<Item> items = initItemData();
        // /List<Category> categorys = initCategoryData();

        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();
        
        List<ReqOrderDto.RequestItem> requestItems = new ArrayList<ReqOrderDto.RequestItem>();

        requestItems.add(ReqOrderDto.RequestItem.builder()
                                        .itemId(items.get(0).getId())
                                        .requestQty(101)
                                        .build());
        
        ReqOrderDto reqOrderDto = ReqOrderDto.builder()
                                        .requestItem(requestItems)
                                        .build();
        
        when(memberService.getMember(reqOrderDto.getMemberId())).thenReturn(null);        
        when(itemService.getItemForUpdate(items.get(0).getId())).thenReturn(items.get(0));
    
        when(orderPersistencePort.save(any(Order.class))).thenReturn(order);
        when(orderItemPersistencePort.save(any(OrderItem.class))).thenReturn(null);

        

        // when then
        assertThrows(ShopException.class, ()->orderService.createOrder(reqOrderDto));
    }

    @Test   // item 정보가 없는 경우 테스트 
    public void createOrderNotFoundItem_test() throws Exception{

        //given
        List<Member> members = initMemberData();
        List<Item> items = initItemData();
        // /List<Category> categorys = initCategoryData();

        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();
        order.setMember(members.get(0));
        
        List<ReqOrderDto.RequestItem> requestItems = new ArrayList<ReqOrderDto.RequestItem>();

        requestItems.add(ReqOrderDto.RequestItem.builder()
                                        .itemId(items.get(0).getId())
                                        .requestQty(101)
                                        .build());
        
        ReqOrderDto reqOrderDto = ReqOrderDto.builder()
                                        .memberId(members.get(0).getId())
                                        .requestItem(requestItems)
                                        .build();
        
        when(memberService.getMember(reqOrderDto.getMemberId())).thenReturn(members.get(0));
        when(itemService.getItemForUpdate(items.get(0).getId())).thenReturn(null);
        // doThrow(ShopException.class).when(commonUtils).nullCheckAndThrowException(null, Item.class.getName());
        
        when(orderPersistencePort.save(any(Order.class))).thenReturn(order);
        when(orderItemPersistencePort.save(any(OrderItem.class))).thenReturn(null);
        

        // when then
        assertThrows(ShopException.class, ()->orderService.createOrder(reqOrderDto));
    }

    @Test
    public void getOrderInfoByOrderId_test() throws Exception {
        
        //given
        List<Member> members = initMemberData();
        List<Item> items = initItemData();
        List<Category> categorys = initCategoryData();

        // order 정보 생성
        Order order = Order.builder()
                        .id(0L)
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();
        // order에 member mapping
        order.setMember(members.get(0));

        // item1, itme2 를 category1 에 mapping
        categorys.get(0).addItem(items.get(0));
        categorys.get(0).addItem(items.get(1));

        // orderitems 생성 및 order에 mapping item1, item2 주문 
        OrderItem orderItem1 = OrderItem.builder()
                                .item(items.get(0))
                                .count(30)
                                .build();

        OrderItem orderItem2 = OrderItem.builder()
                                .item(items.get(1))
                                .count(20)
                                .build();

        order.addOrderItems(orderItem1);
        order.addOrderItems(orderItem2);
        
        // order 정보 조회 시 order 반환
        when(orderPersistencePort.findOrderInfoByOrderId(0L)).thenReturn(order);
        
        // when
        RespOrderDto respOrderDto = orderService.getOrderInfoByOrderId(0L);

        // then 
        assertEquals(members.get(0).getId(), respOrderDto.getMemberId());
        assertEquals(OrderState.REQUEST, respOrderDto.getOrderState());
        
        // 요청 item 개수 확인
        assertEquals(2, respOrderDto.getOrderItemInfos().size());
        // 요청 item id 및 요청 개수 확인
        assertEquals(items.get(0).getId(), respOrderDto.getOrderItemInfos().get(0).getItemId());
        assertEquals(30, respOrderDto.getOrderItemInfos().get(0).getItemRequestQty());

        // 요청 item id 및 요청 개수 확인
        assertEquals(items.get(1).getId(), respOrderDto.getOrderItemInfos().get(1).getItemId());
        assertEquals(20, respOrderDto.getOrderItemInfos().get(1).getItemRequestQty());

    }

    @Test
    public void cancelOrder_test() throws ShopException {
        
        //given
        List<Member> members = initMemberData();
        List<Item> items = initItemData();
        // /List<Category> categorys = initCategoryData();

        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();
        order.setMember(members.get(0));
        
        // orderitems 생성 및 order에 mapping item1, item2 주문 
        OrderItem orderItem1 = OrderItem.builder()
                                .item(items.get(0))
                                .count(30)
                                .build();

        OrderItem orderItem2 = OrderItem.builder()
                                .item(items.get(1))
                                .count(20)
                                .build();

        order.addOrderItems(orderItem1);
        order.addOrderItems(orderItem2);

        when(itemService.getItemForUpdate(items.get(0).getId())).thenReturn(items.get(0));
        when(itemService.getItemForUpdate(items.get(1).getId())).thenReturn(items.get(1));

        when(orderPersistencePort.findById(0L)).thenReturn(order);
        
        // when
        orderService.cancelOrder(0L);

        // 취소 확인
        assertEquals(OrderState.CANCEL, order.getOrderState());
        
        // then 취소 수량 update 확인
        assertEquals(130, items.get(0).getRemainQty());
        assertEquals(120, items.get(1).getRemainQty());
    }

    @Test
    public void cancelOrderException_test() throws ShopException {
        
        //given
        List<Member> members = initMemberData();
        List<Item> items = initItemData();
        // /List<Category> categorys = initCategoryData();

        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.COMPLETE)
                        .build();
        order.setMember(members.get(0));
        
        // orderitems 생성 및 order에 mapping item1, item2 주문 
        OrderItem orderItem1 = OrderItem.builder()
                                .item(items.get(0))
                                .count(30)
                                .build();

        OrderItem orderItem2 = OrderItem.builder()
                                .item(items.get(1))
                                .count(20)
                                .build();

        order.addOrderItems(orderItem1);
        order.addOrderItems(orderItem2);

        when(itemService.getItemForUpdate(items.get(0).getId())).thenReturn(items.get(0));
        when(itemService.getItemForUpdate(items.get(1).getId())).thenReturn(items.get(1));

        when(orderPersistencePort.findById(0L)).thenReturn(order);
        
        // when then OrderState가 REQUEST가 아닐 경우 exception 발생 테스트 
        assertThrows(ShopException.class, ()->orderService.cancelOrder(0L));
    }    

    @Test
    public void updateOrderStatus_test() throws ShopException{

        // given
        Order order = Order.builder()
                        .orderDate(new Date())
                        .orderState(OrderState.REQUEST)
                        .build();

        when(orderPersistencePort.findById(0L)).thenReturn(order);                        

        // when 
        orderService.updateOrderStatus(0L, OrderState.COMPLETE);

        // then
        assertEquals(OrderState.COMPLETE, order.getOrderState());
    }
    

}
