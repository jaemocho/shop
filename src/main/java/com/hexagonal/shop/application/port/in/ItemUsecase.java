package com.hexagonal.shop.application.port.in;

import java.util.List;

import com.hexagonal.shop.application.service.dto.ReqItemDto;
import com.hexagonal.shop.application.service.dto.RespItemDto;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Item;

public interface ItemUsecase {
    public Item addItem(ReqItemDto reqItemDto);

    public void removeItem(Long id) throws ShopException;
    
    public List<RespItemDto> getAllItem();

    public List<RespItemDto> getItemByCategoryId(Long id);

    public RespItemDto getItemById(Long id) throws ShopException;

    public void updateItem(Long id, ReqItemDto reqItemDto) throws ShopException;

    public Item getItem(Long id);

    public Item getItemForUpdate(Long id);

}
