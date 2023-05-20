package com.hexagonal.shop.application.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RespCategoryDto {
    
    private Long id;

    private String name;
}
