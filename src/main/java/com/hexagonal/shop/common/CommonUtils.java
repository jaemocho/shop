package com.hexagonal.shop.common;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.hexagonal.shop.common.Constants.ExceptionClass;
import com.hexagonal.shop.common.exception.ShopException;


@Component
public class CommonUtils {
    private static final String NOT_FOUND = "Not Found ";
    
    public static void nullCheckAndThrowException(Object object, String className){
        if(object == null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, NOT_FOUND +" "+ className); 
        }
    }
}
