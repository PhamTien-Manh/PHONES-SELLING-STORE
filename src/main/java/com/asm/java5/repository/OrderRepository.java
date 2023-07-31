package com.asm.java5.repository;

import com.asm.java5.domain.Customer;
import com.asm.java5.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Transactional
    Order findByCustomerAndStatus(Customer customer, Integer status);

}
