package com.hexagonal.shop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespErrorDto {
    
    private String errorType;
    
    private String code;
    
    private String message;
}
