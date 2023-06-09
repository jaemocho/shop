package com.hexagonal.shop.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hexagonal.shop.application.port.out.CategoryPersistencePort;
import com.hexagonal.shop.application.port.out.ItemPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqItemDto;
import com.hexagonal.shop.application.service.dto.RespItemDto;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Category;
import com.hexagonal.shop.domain.Item;


@ExtendWith(SpringExtension.class)
public class ItemServiceUnitTest {
    
    @Mock
    private ItemPersistencePort itemPersistencePort;

    @Mock
    private CategoryPersistencePort categoryPersistencePort;
    
    @InjectMocks
	private ItemService itemService;

    @Test
    public void addItem_test() {

        //given
        ReqItemDto reqItemDto = ReqItemDto.builder()
                                        .name("T-shirt")
                                        .price(5000)
                                        .remainQty(100)
                                        .categoryId(1L)
                                        .build();

        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Item item = Item.builder()
                    .id(1L)
                    .name("T-shirt")
                    .price(5000)
                    .remainQty(100)
                    .build();


        when(categoryPersistencePort.findById(reqItemDto.getCategoryId())).thenReturn(category);
        when(itemPersistencePort.save(any(Item.class))).thenReturn(item);

        //when
        Item returnitem = itemService.addItem(reqItemDto);

        //then 
        assertEquals("WOMEN", returnitem.getCategory().getName());
    }

    @Test
    public void getAllItem_test() {
        
        //given
        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Item item1 = Item.builder()
                        .id(1L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();

        Item item2 = Item.builder()
                        .id(2L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .build();

        Item item3 = Item.builder()
                        .id(3L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();

        item1.setCategory(category);
        item3.setCategory(category);
                                
        List<Item> items = new ArrayList<Item>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        when(itemPersistencePort.findAll()).thenReturn(items);

        // when
        List<RespItemDto> respItemDtos = itemService.getAllItem();

        //then 
        assertEquals(3, respItemDtos.size());

        int categoryCnt = 0;
        for(RespItemDto dto : respItemDtos) {
            if ("WOMEN".equals(dto.getCategoryName())) {
                categoryCnt++;
            }
        }

        assertEquals(2, categoryCnt);
    }

    @Test
    public void getItemByCategoryId_test() {
        
        //given
        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Item item1 = Item.builder()
                        .id(1L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();
        
        Item item2 = Item.builder()
                        .id(2L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();
        
        Item item3 = Item.builder()
                        .id(3L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .category(category)
                        .build();

        List<Item> items = new ArrayList<Item>();
        items.add(item1);
        items.add(item2);
        items.add(item3);

        when(itemPersistencePort.findByCategoryId(1L)).thenReturn(items);

        // when
        List<RespItemDto> respItemDtos = itemService.getItemByCategoryId(1L);

        //then 
        assertEquals(3, respItemDtos.size());

        int categoryCnt = 0;
        for(RespItemDto dto : respItemDtos) {
            if ("WOMEN".equals(dto.getCategoryName())) {
                categoryCnt++;
            }
        }

        assertEquals(3, categoryCnt);
    }

    @Test
    public void updateItem_test() throws ShopException {
        
        // given
        ReqItemDto reqItemDto = ReqItemDto.builder()
                                    .name("T-shirt")
                                    .price(400)
                                    .remainQty(200)
                                    .build();

        Item item1 = Item.builder()
                        .id(1L)
                        .name("T-shirt")
                        .price(5000)
                        .remainQty(100)
                        .build();
        

        when(itemPersistencePort.findByIdForUpdate(item1.getId())).thenReturn(item1);

        // when 
        itemService.updateItem(item1.getId(), reqItemDto);

        // then
        assertEquals(400, item1.getPrice());
        assertEquals(200, item1.getRemainQty());
        


    }

    @Test // update시도 시 item id가 없을 때
    public void updateItemException_test() throws ShopException {
        
        // given
        Long itemId = 1L;

        ReqItemDto reqItemDto = ReqItemDto.builder()
                            .name("T-shirt")
                            .price(400)
                            .remainQty(200)
                            .build();


        when(itemPersistencePort.findByIdForUpdate(itemId)).thenReturn(null);

        // when then
        assertThrows(ShopException.class, ()->itemService.updateItem(itemId, reqItemDto));
    }

    @Test
    public void getItemById_test() throws ShopException {

        // given
        Item item1 = Item.builder()
                .id(1L)
                .name("T-shirt")
                .price(5000)
                .remainQty(100)
                .build();

        when(itemPersistencePort.findById(item1.getId())).thenReturn(item1);


        // when 
        RespItemDto respItemDto = itemService.getItemById(item1.getId());

        // then
        assertEquals("T-shirt", respItemDto.getName());
        assertEquals(5000, respItemDto.getPrice());
        assertEquals(100, respItemDto.getRemainQty());
        
                
    }

    @Test // 찾는 item id가 없을 때 
    public void getItemByIdException_test() throws ShopException {

        // given
        Long itemId = 1L;
        
        when(itemPersistencePort.findById(itemId)).thenReturn(null);


        // when then
        assertThrows(ShopException.class, ()->itemService.getItemById(itemId));

    }
}
