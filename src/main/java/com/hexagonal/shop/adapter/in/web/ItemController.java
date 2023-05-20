package com.hexagonal.shop.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.shop.application.port.in.ItemUsecase;
import com.hexagonal.shop.application.service.dto.ReqItemDto;
import com.hexagonal.shop.application.service.dto.RespItemDto;
import com.hexagonal.shop.common.exception.ShopException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class ItemController {
    
    private final ItemUsecase itemUsecase;

    @PostMapping(path ="/item")
    public ResponseEntity<?> createItem(@Valid @RequestBody ReqItemDto reqItemDto) {

        itemUsecase.addItem(reqItemDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/item/{id}")
    public ResponseEntity<?> removeItem(@PathVariable("id") Long id) throws ShopException {

        itemUsecase.removeItem(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/item/{id}")
    public ResponseEntity<RespItemDto> getItemById(@PathVariable("id") Long id) throws ShopException{

        return new ResponseEntity<RespItemDto>(itemUsecase.getItemById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/items")
    public ResponseEntity<List<RespItemDto>> getItemByCategoryId(@RequestParam(required = false) Long categoryId) throws ShopException{

        if(categoryId == null) {
            return new ResponseEntity<List<RespItemDto>>(itemUsecase.getAllItem(), HttpStatus.OK);    
        }

        return new ResponseEntity<List<RespItemDto>>(itemUsecase.getItemByCategoryId(categoryId), HttpStatus.OK);
    }

    @PutMapping(path = "/item/{id}")
    public ResponseEntity<?> updateItem(@PathVariable("id") Long id, @Valid @RequestBody ReqItemDto reqItemDto) throws ShopException {

        itemUsecase.updateItem(id, reqItemDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    
}
