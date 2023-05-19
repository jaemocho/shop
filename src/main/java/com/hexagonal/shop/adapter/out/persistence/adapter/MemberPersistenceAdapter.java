package com.hexagonal.shop.adapter.out.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hexagonal.shop.adapter.out.persistence.entity.MemberEntity;
import com.hexagonal.shop.adapter.out.persistence.mapper.DomainEntityMapper;
import com.hexagonal.shop.adapter.out.persistence.repository.MemberRepository;
import com.hexagonal.shop.application.port.out.MemberPersistencePort;
import com.hexagonal.shop.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPersistencePort {
    
    private final MemberRepository memberRepository;

    private final DomainEntityMapper<Member, MemberEntity> memberMapper;

    public Member save(Member member) {
        MemberEntity memberEntity = domainToEntity(member);
        memberEntity = memberRepository.save(memberEntity);
        return memberMapper.entityToDomain(memberEntity);
    }

    public List<Member> findAll() {
        return entityToDomain(memberRepository.findAll());
    }

    public Member findById(String id){
        return entityToDomain(memberRepository.findById(id).orElse(null));
    }

    public void delete(Member member){
        memberRepository.delete(domainToEntity(member));
    }

    public void update(Member member) {
        memberRepository.save(domainToEntity(member));
    }

    private Member entityToDomain(MemberEntity memberEntity) {
        return memberMapper.entityToDomain(memberEntity);
    }

    private List<Member> entityToDomain(List<MemberEntity> memberEntity) {
        return memberMapper.entityToDomain(memberEntity);
    }

    private MemberEntity domainToEntity(Member member) {
        return memberMapper.domainToEntity(member);
    }

}
