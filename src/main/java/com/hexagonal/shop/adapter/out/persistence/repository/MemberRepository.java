package com.hexagonal.shop.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hexagonal.shop.adapter.out.persistence.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String>{
    
}
