package com.hexagonal.shop.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqMemberDto {
    
    @NotNull
    private String id;

    @NotNull
    private String address;

    @NotNull
    private String phoneNumber;

}
