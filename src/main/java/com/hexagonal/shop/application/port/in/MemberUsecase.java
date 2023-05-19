package com.hexagonal.shop.application.port.in;

import java.util.List;

import com.hexagonal.shop.application.service.dto.ReqMemberDto;
import com.hexagonal.shop.application.service.dto.RespMemberDto;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Member;

public interface MemberUsecase {
    public Member addMember(ReqMemberDto reqMemberDto) throws ShopException;

    public void removeMember(String id) throws ShopException;

    public List<RespMemberDto> getAllMember();

    public RespMemberDto getMemberById(String id) throws ShopException;

    public void updateMember(String id, ReqMemberDto reqMemberDto) throws ShopException;

    public Member getMember(String memberId);
}
