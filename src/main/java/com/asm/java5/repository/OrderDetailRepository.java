package com.asm.java5.repository;

import com.asm.java5.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Khi admin tìm kiếm
    @Transactional
    @Query("SELECT new OrderAdmin(o.orderId, count(d.orderDetailId), o.orderDate, o.customer.name, sum(d.unitPrice * d.quantity)) " +
            "FROM Order o " +
            "JOIN OrderDetail d on o.orderId = d.order.orderId WHERE o.status = 1 " +
            "GROUP BY o.orderId, o.orderDate, o.customer.name")
    List<OrderAdmin> findOrderAdminWithStatus();
    // Khi customer tìm kiếm
    @Transactional
    @Query("SELECT new OrderAdmin(o.orderId, count(d.orderDetailId), o.orderDate, o.customer.name, sum(d.unitPrice * d.quantity)) " +
            "FROM Order o " +
            "JOIN OrderDetail d on o.orderId = d.order.orderId WHERE o.status = 1 AND o.customer = :customer " +
            "GROUP BY o.orderId, o.orderDate, o.customer.name")
    List<OrderAdmin> findOrderAdminWithCustomer(@Param("customer") Customer customer);


    @Transactional
    @Query("SELECT d " +
            "FROM Product p " +
            "JOIN OrderDetail d ON d.product.productId = p.productId " +
            "JOIN Order o ON o.orderId = d.order.orderId " +
            "WHERE o.customer =:customer  AND o.status = 0")
    List<OrderDetail> findAllByCustomer(@Param("customer") Customer customer);

    @Query("DELETE FROM OrderDetail d WHERE d.orderDetailId=:id")
    void deleteAll(@Param("id") int id);

    @Transactional
    void deleteOrderDetailByOrderOrderId(int id);
    @Transactional
    List<OrderDetail> findAllByOrderOrderId(int id);

    @Transactional
    OrderDetail findByOrderAndProduct(Order order, Product product);
}
