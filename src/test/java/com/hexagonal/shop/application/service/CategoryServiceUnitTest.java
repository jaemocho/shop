package com.hexagonal.shop.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hexagonal.shop.application.port.out.CategoryPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqCategoryDto;
import com.hexagonal.shop.application.service.dto.RespCategoryDto;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Category;



@ExtendWith(SpringExtension.class)
public class CategoryServiceUnitTest {
    
    @Mock
    private CategoryPersistencePort categoryPersistencePort;
    
    @InjectMocks
	private CategoryService categoryService;

    private List<Category> initCategoryData() {
        
        Category category1 = Category.builder().id(1L).name("WOMEN").build();
        Category category2 = Category.builder().id(2L).name("MEN").build();
        Category category3 = Category.builder().id(3L).name("KIDS").build();

        List<Category> categoryList = new ArrayList<Category>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        return categoryList;

    }

    @Test
    public void addCategory_test() throws ShopException { 

        //given
        ReqCategoryDto reqCategoryDto = ReqCategoryDto.builder()
                                            .name("WOMEN")
                                            .build();
        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        when(categoryPersistencePort.save(category)).thenReturn(category);
        when(categoryPersistencePort.findById(category.getId())).thenReturn(category);

        //when
        categoryService.addCategory(reqCategoryDto);

        //then 
        RespCategoryDto respCategoryDto = categoryService.getCategoryById(category.getId());
        assertEquals("WOMEN", respCategoryDto.getName());
    }

    @Test
    public void getAllCategory_test() throws ShopException {

        //given
        List<Category> categoryList = initCategoryData();
        
        when(categoryPersistencePort.findAll()).thenReturn(categoryList);
        when(categoryPersistencePort.findById(1l)).thenReturn(categoryList.get(0));
        
        //when
        List<RespCategoryDto> respCategoryDtos = categoryService.getAllCategory();
        RespCategoryDto respCategoryDto = categoryService.getCategoryById(1L);

        //then 
        assertEquals(3, respCategoryDtos.size());
        assertEquals("WOMEN", respCategoryDto.getName());
    }

    @Test
    public void getCategoryByName_test() {

        //given
        List<Category> categoryList = initCategoryData();

        when(categoryPersistencePort.findByName("WOMEN")).thenReturn(categoryList);
        
        //when
        List<RespCategoryDto> respCategoryDtos = categoryService.getCategoryByName("WOMEN");

        //then 
        assertEquals(3, respCategoryDtos.size());
    }

    @Test
    public void getCategoryById_test() throws ShopException {

        //given
        List<Category> categoryList = initCategoryData();

        when(categoryPersistencePort.findById(1L)).thenReturn(categoryList.get(0));
        
        //when
        RespCategoryDto respCategoryDto = categoryService.getCategoryById(1L);

        //then 
        assertEquals("WOMEN", respCategoryDto.getName());
    }


    @Test // 찾는 category id 가 없을 때 
    public void getCategoryByIdException_test() throws ShopException {

        //given
        //List<Category> categoryList = initCategoryData();

        when(categoryPersistencePort.findById(1L)).thenReturn(null);
        
        // when then
        assertThrows(ShopException.class, ()-> categoryService.getCategoryById(1L));        
    }

    @Test // category 삭제 시 조회가 안될 때 
    public void removeCategoryException_test() throws ShopException {

        //given
        // List<Category> categoryList = initCategoryData();

        when(categoryPersistencePort.findById(1L)).thenReturn(null);

        // when then
        assertThrows(ShopException.class, ()-> categoryService.removeCategory(1L));
    }



}
