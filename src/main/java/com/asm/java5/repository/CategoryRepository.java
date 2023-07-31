package com.asm.java5.repository;


import com.asm.java5.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Transactional
    Page<Category> findAllByNameLike(String keywords, Pageable pageable);
}
