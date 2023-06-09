package com.hexagonal.shop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexagonal.shop.application.port.in.ItemUsecase;
import com.hexagonal.shop.application.port.out.CategoryPersistencePort;
import com.hexagonal.shop.application.port.out.ItemPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqItemDto;
import com.hexagonal.shop.application.service.dto.RespItemDto;
import com.hexagonal.shop.common.CommonUtils;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Category;
import com.hexagonal.shop.domain.Item;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService implements ItemUsecase{
    
    private final ItemPersistencePort itemPersistencePort;

    private final CategoryPersistencePort categoryPersistencePort;

    @Transactional
    public Item addItem(ReqItemDto reqItemDto) {
        Item item = createNewItem(reqItemDto);
        setCategoryToItem(item, reqItemDto.getCategoryId());
        itemPersistencePort.save(item);
        return item;
    }

    @Transactional
    public void removeItem(Long id) throws ShopException {
        Item item = getItem(id);
        CommonUtils.nullCheckAndThrowException(item, Item.class.getName());
        itemPersistencePort.delete(item);
    }

    @Transactional(readOnly = true)
    public List<RespItemDto> getAllItem() {
        return entityToRespDto(itemPersistencePort.findAll());
    }

    @Transactional(readOnly = true)
    public RespItemDto getItemById(Long id) throws ShopException {
        Item item = getItem(id);
        CommonUtils.nullCheckAndThrowException(item, Item.class.getName());
        return entityToRespDto(item);
    }


    @Transactional(readOnly = true)
    public List<RespItemDto> getItemByCategoryId(Long id) {
        return entityToRespDto(itemPersistencePort.findByCategoryId(id));
    }

    @Transactional
    public void updateItem(Long id, ReqItemDto reqItemDto) throws ShopException{
        Item item = getItemForUpdate(id);
        CommonUtils.nullCheckAndThrowException(item, Item.class.getName());
        item.updateItem(reqItemDto.getName(), reqItemDto.getPrice(), reqItemDto.getRemainQty());
        itemPersistencePort.save(item);
    }

    private void setCategoryToItem(Item item, Long categoryId) {
        Category category = categoryPersistencePort.findById(categoryId);
        if( category != null) {
            item.setCategory(category);
        }
    }

    private Item createNewItem(ReqItemDto reqItemDto) {
        Item item = Item.builder()
                        .name(reqItemDto.getName())
                        .price(reqItemDto.getPrice())
                        .remainQty(reqItemDto.getRemainQty())
                        .build();
        return item;                        
    }

    public Item getItem(Long id) {
        Item item = itemPersistencePort.findById(id);        
        return item;
    }

    public Item getItemForUpdate(Long id) {
        Item item = itemPersistencePort.findByIdForUpdate(id);
        return item;
    }


    private List<RespItemDto> entityToRespDto(List<Item> items){
        
        List<RespItemDto> respItemDtos = new ArrayList<RespItemDto>();

        for (Item i : items) {

            respItemDtos.add(entityToRespDto(i));
        }

        return respItemDtos;
    }

    private RespItemDto entityToRespDto(Item i) {

        Category category = i.getCategory();
        Long categoryId = -1L;
        String categoryName = "";

        if ( category != null) {
            categoryId = category.getId();
            categoryName = category.getName();
        }

        return RespItemDto.builder()
                        .id(i.getId())
                        .name(i.getName())
                        .price(i.getPrice())
                        .remainQty(i.getRemainQty())
                        .categoryId(categoryId)
                        .categoryName(categoryName)
                        .build();
    }
}
