package com.hexagonal.shop.application.port.out;

import java.util.List;

import com.hexagonal.shop.domain.Item;

public interface ItemPersistencePort {
    public Item save(Item item);

    public Item findById(Long id);

    public void delete(Item item);

    public List<Item> findAll();

    public List<Item> findByCategoryId(Long id);

    public Item findByIdForUpdate(Long id);
}
