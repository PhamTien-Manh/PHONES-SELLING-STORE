package com.asm.java5.restcontroller;

import com.asm.java5.domain.Category;
import com.asm.java5.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class CategoryRestController {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/rest/admin/categories")
    public List<Category> getAll(){
        return categoryRepository.findAll();
    }
}
