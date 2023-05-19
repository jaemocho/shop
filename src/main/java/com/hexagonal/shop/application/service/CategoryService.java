package com.hexagonal.shop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexagonal.shop.application.port.in.CategoryUsecase;
import com.hexagonal.shop.application.port.out.CategoryPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqCategoryDto;
import com.hexagonal.shop.application.service.dto.RespCategoryDto;
import com.hexagonal.shop.common.CommonUtils;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Category;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService implements CategoryUsecase{
    
    private final CategoryPersistencePort categoryPersistencePort;

    // category start
    @Transactional
    public void addCategory(ReqCategoryDto reqCategoryDto) {
        Category category = createNewCategory(reqCategoryDto);
        categoryPersistencePort.save(category);
    }

    private Category createNewCategory(ReqCategoryDto reqCategoryDto){
        Category category = Category.builder()
                                .name(reqCategoryDto.getName())
                                .build();
        return category;                                
    }

    @Transactional
    public void removeCategory(Long id) throws ShopException{
        // item 삭제를 해야하나 ?
        Category category = getCategory(id);
        CommonUtils.nullCheckAndThrowException(category, Category.class.getName());
        categoryPersistencePort.delete(category);
    }

    @Transactional(readOnly = true)
    public List<RespCategoryDto> getAllCategory() {
        return entityToRespDto(categoryPersistencePort.findAll());
    }

    @Transactional(readOnly = true)
    public List<RespCategoryDto> getCategoryByName(String name) {
        return entityToRespDto(categoryPersistencePort.findByName(name));
    }

    @Transactional(readOnly = true)
    public RespCategoryDto getCategoryById(Long id) throws ShopException {
        Category category = getCategory(id);
        CommonUtils.nullCheckAndThrowException(category, Category.class.getName());
        return entityToRespDto(category);
    }

    private Category getCategory(Long id) {
        Category category = categoryPersistencePort.findById(id);
        return category;
    }

    private List<RespCategoryDto> entityToRespDto(List<Category> categorys){
        List<RespCategoryDto> respCategoryDtos = new ArrayList<RespCategoryDto>();

        for (Category c : categorys) {
            respCategoryDtos.add(entityToRespDto(c));
        }
        return respCategoryDtos;
    }

    private RespCategoryDto entityToRespDto(Category c) {

        if ( c == null ) return null;

        return RespCategoryDto.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .build();
    }
}
