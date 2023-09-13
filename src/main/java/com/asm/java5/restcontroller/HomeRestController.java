package com.asm.java5.restcontroller;

import com.asm.java5.domain.Product;
import com.asm.java5.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
public class HomeRestController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/rest/home")
    public List<Product> getProductDiscount(){
        List<Product> products = productRepository.findAllByDiscountGreaterThan(0);
        return products;
    }

    @GetMapping("/rest/home/{idDetail}")
    public Product getProductDetail(@PathVariable("idDetail") Integer productId){
        Product product = productRepository.findById(productId).get();
        return product;
    }

    @GetMapping("/rest/home/category/{category}")
    public List<Product> getProductByCategory(@PathVariable("category") Integer category){
        List<Product> products = productRepository.findAllByCategoryCategoryId(category);
        return products;
    }
}
