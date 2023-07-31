package com.asm.java5.controller.customer;

import com.asm.java5.domain.Category;
import com.asm.java5.domain.Product;
import com.asm.java5.repository.CategoryRepository;
import com.asm.java5.repository.ProductRepository;
import com.asm.java5.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("products")
public class ProductCustomerController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StorageService storageService;

    @RequestMapping("")
    public String list(
            @RequestParam("category") int categoryId, Model model
    ){
        Category category = categoryRepository.findById(categoryId).get();
        List<Product> products = productRepository.findAllByCategoryCategoryId(categoryId);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("detail")
    public String productDetail(@RequestParam("id") Integer id, Model model){
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        return "forward:/products";
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
