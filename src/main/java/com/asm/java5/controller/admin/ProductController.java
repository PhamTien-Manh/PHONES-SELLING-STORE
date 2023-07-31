package com.asm.java5.controller.admin;


import com.asm.java5.domain.Category;
import com.asm.java5.domain.Customer;
import com.asm.java5.domain.Product;
import com.asm.java5.repository.CategoryRepository;
import com.asm.java5.repository.ProductRepository;
import com.asm.java5.service.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("admin/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    StorageService storageService;

    private boolean save;

    @ModelAttribute("products")
    public Page<Product> getPro(@RequestParam("page") Optional<Integer> page
            , @RequestParam("keyword") Optional<String> keyword, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 5);
        Page<Product> products = productRepository.findAllByNameLike("%" + keyword.orElse("") + "%",pageable);
        model.addAttribute("keyword", keyword.orElse(""));
        return products;
    }

    @ModelAttribute("categories")
    public List<Category> getCate(){
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }
    @RequestMapping("")
    public String list(@ModelAttribute("product") Product product, Model model){
        if(save == true) {
            model.addAttribute("message", "Lưu thành công!");
            save = false;
        }
        return "admin/manager-product";
    }
    @GetMapping("delete")
    public String delete(@RequestParam("id") Integer id, Model model){
        productRepository.deleteById(id);
        model.addAttribute("message", "Xóa thành công!");
        return "forward:/admin/products";
    }
    @GetMapping("edit")
    public String edit(Model model, @RequestParam("id") Integer id){
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        return "admin/manager-product";
    }
    @PostMapping("update-or-create")
    public String save(Model model, @Valid @ModelAttribute("product") Product product,
            BindingResult result, @RequestParam("imageFile") MultipartFile imageFile) {
        if(result.hasErrors()){
            return "admin/manager-product";
        }
        product.setEnteredDate(new Date());
        if (!imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            product.setImage(storageService.getStoredFilename(imageFile, uuString));
            storageService.store(imageFile, product.getImage());
        }
        productRepository.save(product);
        save = true;
        return "redirect:/admin/products";
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("search")
    public String search(Model model, @RequestParam("keyword") Optional<String> keyword,
                         @RequestParam("page") Optional<Integer> p){
        Pageable pageable = PageRequest.of(p.orElse(0), 5);
        Page<Product> products  = productRepository.findAllByNameLike("%" + keyword.orElse("") + "%", pageable);
        model.addAttribute("keyword", keyword.orElse(""));
        model.addAttribute("product", new Product());
        model.addAttribute("products", products);
        return "admin/manager-product";
    }
}


