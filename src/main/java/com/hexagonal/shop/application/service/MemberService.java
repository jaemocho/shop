package com.hexagonal.shop.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexagonal.shop.application.port.in.MemberUsecase;
import com.hexagonal.shop.application.port.out.MemberPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqMemberDto;
import com.hexagonal.shop.application.service.dto.RespMemberDto;
import com.hexagonal.shop.common.CommonUtils;
import com.hexagonal.shop.common.Constants.ExceptionClass;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService implements MemberUsecase{
    
    private final MemberPersistencePort memberPersistencePort;

    @Transactional
    public Member addMember(ReqMemberDto reqMemberDto) throws ShopException {
        vaildateDuplicateMember(reqMemberDto.getId());
        Member newMember = createNewMember(reqMemberDto);
        return memberPersistencePort.save(newMember);
    }

    @Transactional
    public void removeMember(String id) throws ShopException{

        Member member = getMember(id);
        CommonUtils.nullCheckAndThrowException(member, Member.class.getName());
        
        // member가 가지고 있는 order list 확인
        // order list가 있는 경우 orderitem을 확인 한 후 삭제
        // item 의 remainQty update 
        if ( member.getOrders() != null && member.getOrders().size() > 0 ) {

        }

        // member 가 삭제 될 때 order 까지 삭제 되면 
        // 주문 수량을 되돌려 놓지 못하니까 orphanRemoval 없이 
        memberPersistencePort.delete(member);

    }

    @Transactional(readOnly = true)
    public List<RespMemberDto> getAllMember() {
        return entityToRespDto(memberPersistencePort.findAll());
    }

    @Transactional(readOnly = true)
    public RespMemberDto getMemberById(String id) throws ShopException{
        Member member = getMember(id);
        CommonUtils.nullCheckAndThrowException(member, Member.class.getName());
        return entityToRespDto(member);
    }

    @Transactional
    public void updateMember(String id, ReqMemberDto reqMemberDto) throws ShopException {
        Member member = getMember(id);
        CommonUtils.nullCheckAndThrowException(member, Member.class.getName());
        member.updateMember(reqMemberDto.getAddress(), reqMemberDto.getPhoneNumber());
        memberPersistencePort.update(member);   
    }

    @Transactional(readOnly = true)
    public Member getMember(String memberId) {
        Member member = memberPersistencePort.findById(memberId);
        return member;
    }

    private Member createNewMember(ReqMemberDto reqMemberDto) {
        Member newMember = Member.builder()
                            .id(reqMemberDto.getId()) 
                            .address(reqMemberDto.getAddress())
                            .phoneNumber(reqMemberDto.getPhoneNumber())
                            .build();
        return newMember;
    }

    private void vaildateDuplicateMember(String memberId) {
        
        Member member = memberPersistencePort.findById(memberId);

        if( member != null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "already exist memeber"); 
        }
    }

    private List<RespMemberDto> entityToRespDto(List<Member> members) {
        
        List<RespMemberDto> respMemberDtos = new ArrayList<RespMemberDto>();

        for(Member m : members) {
            respMemberDtos.add(entityToRespDto(m));
        }

        return respMemberDtos;
    }

    private RespMemberDto entityToRespDto(Member m) {
        return RespMemberDto.builder()
                        .id(m.getId())
                        .address(m.getAddress())
                        .phoneNumber(m.getPhoneNumber())
                        .build();
    }
    
}
