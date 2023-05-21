package com.hexagonal.shop.out.persistence.adapter.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@AutoConfigureTestDatabase(replace= Replace.ANY) 
@DataJpaTest
public class BaseRepositoryTest {
    
}
