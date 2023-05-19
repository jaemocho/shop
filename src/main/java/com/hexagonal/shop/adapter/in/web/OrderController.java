package com.hexagonal.shop.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.shop.application.port.in.OrderUsecase;
import com.hexagonal.shop.application.service.dto.ReqOrderDto;
import com.hexagonal.shop.application.service.dto.RespOrderDto;
import com.hexagonal.shop.common.exception.ShopException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class OrderController {

    private final OrderUsecase orderUsecase;

    @PostMapping(path ="/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody ReqOrderDto reqOrderDto) throws ShopException{
        
        Long orderId = orderUsecase.createOrder(reqOrderDto);

        return new ResponseEntity<>(orderId, HttpStatus.CREATED);
    }

    @DeleteMapping(path="/order/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id) throws ShopException {

        orderUsecase.cancelOrder(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/order/{id}")
    public ResponseEntity<RespOrderDto> getOrderInfoByOrderId(@PathVariable("id") Long id) throws ShopException {
        
        return new ResponseEntity<RespOrderDto>(orderUsecase.getOrderInfoByOrderId(id), HttpStatus.OK);

    }

    @GetMapping(path = "/order/member/{memberId}")
    public ResponseEntity<List<RespOrderDto>> getOrderInfoByMemberId(@PathVariable("memberId") String memberId) throws ShopException {
        

        return new ResponseEntity<List<RespOrderDto>>(orderUsecase.getOrderInfoByMemberId(memberId), HttpStatus.OK);

    }
    
}
