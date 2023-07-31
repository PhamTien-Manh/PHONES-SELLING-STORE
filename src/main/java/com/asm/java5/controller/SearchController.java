package com.asm.java5.controller;

import com.asm.java5.domain.Product;
import com.asm.java5.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("search/detail")
    public String productDetail(Model model, @RequestParam("query") String query, @RequestParam("id") Integer id){
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        List<Product> products = productRepository.findAllByNameLike("%" + query + "%");
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "search";
    }

    @PostMapping("search")
    public String search(Model model, @RequestParam("query") String query){
        if(query.equals("")) return "redirect:/index";
        List<Product> products = productRepository.findAllByNameLike("%" + query + "%");
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        return "search";
    }
}
