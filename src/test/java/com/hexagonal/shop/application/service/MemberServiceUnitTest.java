package com.hexagonal.shop.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hexagonal.shop.application.port.out.MemberPersistencePort;
import com.hexagonal.shop.application.service.dto.ReqMemberDto;
import com.hexagonal.shop.application.service.dto.RespMemberDto;
import com.hexagonal.shop.common.exception.ShopException;
import com.hexagonal.shop.domain.Member;


@ExtendWith(SpringExtension.class)
public class MemberServiceUnitTest {
    
    @Mock
    private MemberPersistencePort memberPersistencePort;

    @InjectMocks
    private MemberService memberService;

    private List<Member> initMemberData() {


        List<Member> members = new ArrayList<Member>();

        Member member1 = Member.builder()
                            .id("member1")
                            .address("수원")
                            .phoneNumber("0101112222")
                            .build();

        Member member2 = Member.builder()
                            .id("member2")
                            .address("서울")
                            .phoneNumber("0101113333")
                            .build();        

        Member member3 = Member.builder()
                            .id("member3")
                            .address("대전")
                            .phoneNumber("0101114444")
                            .build();                                

        members.add(member1);
        members.add(member2);
        members.add(member3);

        return members;
    }

    @Test
    public void addMember_test() throws ShopException {

        //given 
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .id("jjm")
                                            .address("수원")
                                            .phoneNumber("010333322222")
                                            .build();

        
        Member member = Member.builder()
                            .id("jjm")
                            .address("수원")
                            .phoneNumber("010333322222")
                            .build();

        when(memberPersistencePort.save(any(Member.class))).thenReturn(member);

        //when 
        Member returnMember = memberService.addMember(reqMemberDto);

        //then
        assertEquals("jjm", returnMember.getId());
                                            
    }

    @Test // 등록하려는 member id 가 이미 등록되어있을 때 
    public void addMemberException_test() throws ShopException {

        //given 
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .id("jjm")
                                            .address("수원")
                                            .phoneNumber("010333322222")
                                            .build();

        
        Member member = Member.builder()
                            .id("jjm")
                            .address("수원")
                            .phoneNumber("010333322222")
                            .build();

        when(memberPersistencePort.findById("jjm")).thenReturn(member);
        when(memberPersistencePort.save(any(Member.class))).thenReturn(member);

        // when then
        assertThrows(ShopException.class, ()->memberService.addMember(reqMemberDto));               
    }
    
    @Test
    public void getAllMember_test(){

        // given
        List<Member> members = initMemberData();

        when(memberPersistencePort.findAll()).thenReturn(members);

        // when 
        List<RespMemberDto> respMemberDtos = memberService.getAllMember();

        // then
        assertEquals(3, respMemberDtos.size());

    }

    @Test
    public void getMemberById_test() throws ShopException {

        // given
        List<Member> members = initMemberData();

        Member member = members.get(0);

        when(memberPersistencePort.findById(member.getId())).thenReturn(member);

        // when
        RespMemberDto respMemberDto = memberService.getMemberById(member.getId());

        // then
        assertEquals(member.getId(), respMemberDto.getId());
    }

    @Test // id가 없을 때 
    public void getMemberByIdException_test() throws ShopException {

        // given
        List<Member> members = initMemberData();

        Member member = members.get(0);

        when(memberPersistencePort.findById(member.getId())).thenReturn(null);


        // when then
        assertThrows(ShopException.class, ()->memberService.getMemberById(member.getId()));

    }

    @Test
    public void updateMember_test() throws ShopException {

        // given
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .phoneNumber("01122223333")
                                            .address("제주도")
                                            .build();

        List<Member> members = initMemberData();

        Member member = members.get(0);

        when(memberPersistencePort.findById(member.getId())).thenReturn(member);

        // when
        memberService.updateMember(member.getId(), reqMemberDto);

        // then
        assertEquals("01122223333", member.getPhoneNumber());
        assertEquals("제주도", member.getAddress());

    }

    @Test // update하려는 member id가 없을 때 
    public void updateMemberException_test() throws ShopException {

        // given
        ReqMemberDto reqMemberDto = ReqMemberDto.builder()
                                            .phoneNumber("01122223333")
                                            .address("제주도")
                                            .build();

        List<Member> members = initMemberData();

        Member member = members.get(0);

        when(memberPersistencePort.findById(member.getId())).thenReturn(null);

        // when then
        assertThrows(ShopException.class, ()->memberService.updateMember(member.getId(), reqMemberDto));

    }

    @Test // 삭제하려는  member id가 없을 때 
    public void removeMemberException_test() throws ShopException {

        // given
        when(memberPersistencePort.findById("memberA")).thenReturn(null);

        // when then
        assertThrows(ShopException.class, ()->memberService.removeMember("member"));

    }


}
