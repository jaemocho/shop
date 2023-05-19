package com.hexagonal.shop.adapter.out.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexagonal.shop.adapter.out.persistence.entity.ItemEntity;
import com.hexagonal.shop.adapter.out.persistence.mapper.DomainEntityMapper;
import com.hexagonal.shop.adapter.out.persistence.repository.ItemRepository;
import com.hexagonal.shop.application.port.out.ItemPersistencePort;
import com.hexagonal.shop.domain.Item;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemPersistenceAdapter implements ItemPersistencePort{
    
    private final ItemRepository itemRepository;
    
    private final DomainEntityMapper<Item, ItemEntity> itemMapper;

    public Item save(Item item){
        ItemEntity itemEntity = domainToEntity(item);
        itemEntity = itemRepository.save(itemEntity);
        return entityToDomain(itemEntity);
    }

    public void delete(Item item) {
        itemRepository.delete(domainToEntity(item));
    }

    public Item findById(Long id) {
        return entityToDomain(itemRepository.findById(id).orElse(null));
    }

    public List<Item> findAll() {
        return entityToDomain(itemRepository.findAll());
    }

    public List<Item> findByCategoryId(Long id){
        return entityToDomain(itemRepository.findByCategoryId(id));
    }

    public Item findByIdForUpdate(Long id){
        return entityToDomain(itemRepository.findByIdForUpdate(id));
    }

    private Item entityToDomain(ItemEntity itemEntity) {
        return itemMapper.entityToDomain(itemEntity);
    }

    private List<Item> entityToDomain(List<ItemEntity> itemEntities) {
        return itemMapper.entityToDomain(itemEntities);
    }

    private ItemEntity domainToEntity(Item item) {
        return itemMapper.domainToEntity(item);
    }
}
