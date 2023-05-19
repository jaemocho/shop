package com.hexagonal.shop.adapter.in.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RespMemberDto {
    
    private String id;

    private String address;

    private String phoneNumber;

}
