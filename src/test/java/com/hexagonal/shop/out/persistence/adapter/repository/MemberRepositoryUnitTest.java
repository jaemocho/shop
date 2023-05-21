package com.hexagonal.shop.out.persistence.adapter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hexagonal.shop.adapter.out.persistence.entity.MemberEntity;
import com.hexagonal.shop.adapter.out.persistence.repository.MemberRepository;

public class MemberRepositoryUnitTest extends BaseRepositoryTest {
    
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void MemberEntity_test(){

        // 초기 MemberEntity date insert 
        MemberEntity memberEntity1 = MemberEntity.builder()
                            .id("MemberEntityA")
                            .address("서울 여기 저기")
                            .phoneNumber("010 1234 5678")
                            .build();

        MemberEntity memberEntity2 = MemberEntity.builder()
                            .id("MemberEntityB")
                            .address("수원 여기 저기")
                            .phoneNumber("010 1234 7890")
                            .build();

        memberRepository.save(memberEntity1);
        memberRepository.save(memberEntity2);

        memberRepository.flush();

        // 전체 MemberEntity 조회 테스트 
        List<MemberEntity> MemberEntitys = memberRepository.findAll(); 
        assertEquals(2, MemberEntitys.size());
        
        // MemberEntityA 조회 테스트 
        MemberEntity findMemberEntity = memberRepository.findById("MemberEntityA").orElse(null);
        assertEquals("서울 여기 저기", findMemberEntity.getAddress());

        // MemberEntityA 정보 update 테스트 
        findMemberEntity.updateMember("부산 여기 저기", findMemberEntity.getPhoneNumber());
        memberRepository.flush();
        findMemberEntity = memberRepository.findById("MemberEntityA").orElse(null);
        assertEquals("부산 여기 저기", findMemberEntity.getAddress());

        memberRepository.delete(findMemberEntity);
        memberRepository.flush();
        assertEquals(1 ,memberRepository.findAll().size());
    }
}
