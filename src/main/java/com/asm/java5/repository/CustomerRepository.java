package com.asm.java5.repository;

import com.asm.java5.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Transactional
    Customer findByEmail(String email);

    @Transactional
    Page<Customer> findAllByNameLike(String keywords, Pageable pageable);

}
