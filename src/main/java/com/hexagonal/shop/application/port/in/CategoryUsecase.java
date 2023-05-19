package com.hexagonal.shop.application.port.in;

import java.util.List;

import com.hexagonal.shop.adapter.in.web.dto.ReqCategoryDto;
import com.hexagonal.shop.adapter.in.web.dto.RespCategoryDto;
import com.hexagonal.shop.common.exception.ShopException;

public interface CategoryUsecase {
    public void addCategory(ReqCategoryDto reqCategoryDto);
    
    public void removeCategory(Long id) throws ShopException;

    public List<RespCategoryDto> getAllCategory();

    public List<RespCategoryDto> getCategoryByName(String name);

    public RespCategoryDto getCategoryById(Long id) throws ShopException;
}
