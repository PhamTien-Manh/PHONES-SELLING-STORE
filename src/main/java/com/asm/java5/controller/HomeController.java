package com.asm.java5.controller;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Product;
import com.asm.java5.repository.CategoryRepository;
import com.asm.java5.repository.ProductRepository;
import com.asm.java5.service.CookieService;
import com.asm.java5.service.CustomerService;
import com.asm.java5.service.SessionService;
import com.asm.java5.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    StorageService storageService;
    @Autowired
    CookieService cookieService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CustomerService customerService;

    @ModelAttribute("products")
    public List<Product> getProductDiscount(){
        List<Product> products = productRepository.findAllByDiscountGreaterThan(0);
        return products;
    }
    @RequestMapping("/oauth2")
    public String success(OAuth2AuthenticationToken oauth2) throws IOException {
        try{
            customerService.loginFromOAuth2(oauth2);
        } catch (Exception e){
            cookieService.exception("Email của bạn đã được sử dụng cho tài khoản khác", request, response);
        }
        return "redirect:/index";
    }
    @GetMapping("index")
    public String index(){
        return "index";
    }

//    @GetMapping("index/detail")
//    public String productDetail(@RequestParam("id") Integer id, Model model){
//        Product product = productRepository.findById(id).get();
//        model.addAttribute("product", product);
//        return "forward:/index";
//    }

    @GetMapping("/customer/error")
    public String error(){
        return "pages/pages-error-404";
    }

    @RequestMapping("{page}")
    public String page(@PathVariable("page") String page){
        switch (page){
            //PAGE
            case "page-contact"   : return "pages/pages-contact";
            case "page-faq"   : return "pages/pages-faq";
        }
        return null;
    }

    @GetMapping("/index/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
