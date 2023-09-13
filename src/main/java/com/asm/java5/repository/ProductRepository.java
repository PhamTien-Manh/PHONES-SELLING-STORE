package com.asm.java5.repository;

import com.asm.java5.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategoryCategoryId(Integer id);

    List<Product> findAllByDiscountGreaterThan(Integer discount);

    List<Product> findAllByNameLike(String query);

    Page<Product> findAllByNameLike(String keywords, Pageable pageable);
}
