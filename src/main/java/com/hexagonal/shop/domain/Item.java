package com.hexagonal.shop.domain;

import org.springframework.http.HttpStatus;

import com.hexagonal.shop.common.Constants.ExceptionClass;
import com.hexagonal.shop.common.exception.ShopException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    
    private Long id;

    private String name;
    
    private int price;
    
    private int remainQty;

    private Category category;

    public void setCategory(Category category) {
        this.category = category;

        if(!category.getItems().contains(this)){
            category.getItems().add(this);
        }
    }

    public void updateItem(String name, int price, int remainQty){
        this.name = name;
        this.price = price;
        this.remainQty = remainQty;
    }

    public void addRemainQty(int requestQty) {
        this.remainQty += requestQty;
    }

    public void removeRemainQty(int requestQty)  {
        if ( requestQty > this.remainQty ) {
            throw new ShopException(ExceptionClass.SHOP, HttpStatus.BAD_REQUEST, "not enough remainQty");
        }
        this.remainQty -= requestQty;
    }

}
