package com.hexagonal.shop.adapter.out.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.domain.Item;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final CategoryMapper categoryMapper;

    public List<ItemEntity> domainToEntity(List<Item> items) {
        List<ItemEntity> itemEntities = new ArrayList<ItemEntity>();
        for(Item i : items) {
            itemEntities.add(domainToEntity(i));
        }
        return itemEntities;   
    }

    public ItemEntity domainToEntity(Item item) {
        if ( item == null ) {
            return null;
        }
        return ItemEntity.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .price(item.getPrice())
                            .remainQty(item.getRemainQty())
                            .categoryEntity(categoryMapper.domainToEntity(item.getCategory()))
                            .build();
    }

    public List<Item> entityToDomain(List<ItemEntity> itemEntities)  {
        List<Item> items = new ArrayList<Item>();
        for(ItemEntity i : itemEntities) {
            items.add(entityToDomain(i));
        }
        return items;
    }

    public Item entityToDomain(ItemEntity itemEntity) {
        if( itemEntity == null ) {
            return null;
        }
        return Item.builder()
                    .id(itemEntity.getId())
                    .name(itemEntity.getName())
                    .price(itemEntity.getPrice())
                    .remainQty(itemEntity.getRemainQty())
                    .category(categoryMapper.entityToDomain(itemEntity.getCategoryEntity()))
                    .build();
    }
}
