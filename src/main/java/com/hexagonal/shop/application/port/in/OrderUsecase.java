package com.hexagonal.shop.application.port.in;

import java.util.List;

import com.hexagonal.shop.application.service.dto.ReqOrderDto;
import com.hexagonal.shop.application.service.dto.RespOrderDto;
import com.hexagonal.shop.common.ShopConstants.OrderState;
import com.hexagonal.shop.common.exception.ShopException;

public interface OrderUsecase {
    public Long createOrder(ReqOrderDto reqOrderDto) throws ShopException;

    public RespOrderDto getOrderInfoByOrderId(Long orderId) throws ShopException;

    public List<RespOrderDto> getOrderInfoByMemberId(String memberId);

    public void cancelOrder(Long orderId) throws ShopException;

    public void updateOrderStatus(Long orderId, OrderState orderState) throws ShopException;
}
