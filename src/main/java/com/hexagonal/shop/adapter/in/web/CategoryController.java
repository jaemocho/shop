package com.hexagonal.shop.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonal.shop.adapter.in.web.dto.ReqCategoryDto;
import com.hexagonal.shop.adapter.in.web.dto.RespCategoryDto;
import com.hexagonal.shop.application.port.in.CategoryUsecase;
import com.hexagonal.shop.common.exception.ShopException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shop")
public class CategoryController {
    
    private final CategoryUsecase categoryUsecase;

    @PostMapping(path = "/category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody ReqCategoryDto reqCategoryDto) {
        
        categoryUsecase.addCategory(reqCategoryDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/category/{id}")
    public ResponseEntity<?> removeCategory(@PathVariable("id") Long id) throws ShopException {

        categoryUsecase.removeCategory(id);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(path = "/category/{id}")
    public ResponseEntity<RespCategoryDto> getCategoryById(@PathVariable("id") Long id) throws ShopException{

        return new ResponseEntity<RespCategoryDto>(categoryUsecase.getCategoryById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/categorys")
    public ResponseEntity<List<RespCategoryDto>> getCategoryByName(@RequestParam(required = false) String name) throws ShopException{

        if ( name == null || "".equals(name)) {
            return new ResponseEntity<List<RespCategoryDto>>(categoryUsecase.getAllCategory(), HttpStatus.OK);
        }

        return new ResponseEntity<List<RespCategoryDto>>(categoryUsecase.getCategoryByName(name), HttpStatus.OK);
    }

}
