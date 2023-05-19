package com.hexagonal.shop.application.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexagonal.shop.application.port.in.OrderUsecase;
import com.hexagonal.shop.application.port.out.ItemPersistencePort;
import com.hexagonal.shop.application.port.out.OrderItemPersistencePort;
import com.hexagonal.shop.application.port.out.OrderPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqOrderDto;
import com.hexagonal.shop.application.service.dto.RespOrderDto;
import com.hexagonal.shop.common.CommonUtils;
import com.hexagonal.shop.common.Constants.ExceptionClass;
import com.hexagonal.shop.common.ShopConstants.OrderState;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Item;
import com.hexagonal.shop.domain.Member;
import com.hexagonal.shop.domain.Order;
import com.hexagonal.shop.domain.OrderItem;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService implements OrderUsecase {

    private final OrderPersistencePort orderPersistencePort;
    
    private final OrderItemPersistencePort orderItemPersistencePort;

    private final ItemPersistencePort itemPersistencePort;

    private final MemberService memberService;

    private final ItemService itemService;


    @Transactional
    public Long createOrder(ReqOrderDto reqOrderDto) throws ShopException {
        Member orderMember = memberService.getMember(reqOrderDto.getMemberId());
        CommonUtils.nullCheckAndThrowException(orderMember, Member.class.getName());
        
        Order order = createNewOrder();
        order.setMember(orderMember);
        order = orderPersistencePort.save(order);
        
        addOrderItemToOrder(reqOrderDto, order);
        return order.getId();
    }


    @Transactional(readOnly = true)
    public RespOrderDto getOrderInfoByOrderId(Long orderId) throws ShopException {

        // 상세 정보 필요해서 fetch join 사용 
        Order order = orderPersistencePort.findOrderInfoByOrderId(orderId);
        CommonUtils.nullCheckAndThrowException(order, Order.class.getName());
        return entityToRespDto(order);
    }

    @Transactional(readOnly = true)
    public List<RespOrderDto> getOrderInfoByMemberId(String memberId) {
        // 상세 정보 필요해서 fetch join 사용 
        return entityToRespDto(orderPersistencePort.findOrderInfoByMemberId(memberId));
    }


    @Transactional
    public void cancelOrder(Long orderId) throws ShopException {
        Order order = orderPersistencePort.findById(orderId);
        CommonUtils.nullCheckAndThrowException(order, Order.class.getName());
        vaildateOrderStateForCancel(order);
        updateOrderStatus(order, OrderState.CANCEL);
        cancelOrderItem(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderState orderState) throws ShopException {
        // order 는 공유자원이 아니라 for update 없이 
        Order order = orderPersistencePort.findById(orderId);
        CommonUtils.nullCheckAndThrowException(order, Order.class.getName());
        updateOrderStatus(order, orderState);
    }

    private Order createNewOrder() {
        Order order = Order.builder()
                            .orderDate(new Date())
                            .orderState(OrderState.REQUEST)
                            .build();
        return order;
    }

    private void updateOrderStatus(Order order, OrderState orderState) {
        order.updateOrderStatus(orderState);
        orderPersistencePort.save(order);
    }


    private void vaildateOrderStateForCancel(Order order) {
        if( !OrderState.REQUEST.equals(order.getOrderState())) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST
            , "REQUEST 상태일 때만 취소 가능합니다. 관리자에게 문의하세요");
        }
    }

    
    private void addOrderItemToOrder(ReqOrderDto reqOrderDto, Order order) {
        OrderItem orderItem;
        Item item;
        for( ReqOrderDto.RequestItem requestItem : reqOrderDto.getRequestItem() ) {
            item = itemService.getItemForUpdate(requestItem.getItemId());
            CommonUtils.nullCheckAndThrowException(item, Item.class.getName());
            itemRemoveRemainQty(item, requestItem.getRequestQty());
            orderItem = createOrderItem(item, requestItem.getRequestQty());
            setOrderToOrderItem(orderItem, order);
        }
    }

    private void itemRemoveRemainQty(Item item, int requestQty) {
        item.removeRemainQty(requestQty);
        itemPersistencePort.save(item);
    }

    private void setOrderToOrderItem(OrderItem orderItem, Order order) {
        orderItem.setOrder(order);
        orderItemPersistencePort.save(orderItem);
    }

    private OrderItem createOrderItem(Item item, int requestQty) {
        OrderItem orderItem = OrderItem.builder()
                                    .item(item)
                                    .count(requestQty)
                                    .build();
        return orderItem;                                    
    }


    private void cancelOrderItem(Order order) {
        Item item; 
        for ( OrderItem oi : order.getOrderItems()) {
            item = itemService.getItemForUpdate(oi.getItem().getId());
            if ( item == null ) continue;
            itemAddRemainQty(item, oi.getCount());
        }
    }

    private void itemAddRemainQty(Item item, int count) {
        item.addRemainQty(count);
        itemPersistencePort.save(item);
    }


    private List<RespOrderDto> entityToRespDto(List<Order> orders) {
        List<RespOrderDto> respOrderDtos = new ArrayList<RespOrderDto>();

        for( Order o : orders ) {
            respOrderDtos.add(entityToRespDto(o));
        }
        return respOrderDtos;
    }

    private RespOrderDto entityToRespDto(Order order) {

        List<RespOrderDto.OrderItemInfo> orderIteminfos
                 = new ArrayList<RespOrderDto.OrderItemInfo>();
        
        // orderItems resp dto로 반환 
        for( OrderItem o : order.getOrderItems()) {

            orderIteminfos.add(RespOrderDto.OrderItemInfo.builder()
                                        .itemId(o.getItem().getId())
                                        .itemName(o.getItem().getName())
                                        .itemPrice(o.getItem().getPrice())
                                        .itemRequestQty(o.getCount())
                                        .categoryId(o.getItem().getCategory().getId())
                                        .categoryName(o.getItem().getCategory().getName())
                                        .build());
        }

        return RespOrderDto.builder()
                        .orderId(order.getId())
                        .memberId(order.getMember().getId())
                        .orderItemInfos(orderIteminfos)
                        .orderDate(order.getOrderDate())
                        .orderState(order.getOrderState())
                        .build();
                        
    }
}
