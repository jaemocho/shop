package com.hexagonal.shop.application.port.out;

import java.util.List;

import com.hexagonal.shop.domain.Member;

public interface MemberPersistencePort {
    public Member save(Member member);
        
    public List<Member> findAll();

    public Member findById(String id);

    public void delete(Member member);

    public void update(Member member);

}
