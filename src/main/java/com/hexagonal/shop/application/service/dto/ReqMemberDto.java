package com.hexagonal.shop.application.service.dto;

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
