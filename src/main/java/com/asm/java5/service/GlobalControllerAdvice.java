package com.asm.java5.service;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Category;
import com.asm.java5.domain.Customer;
import com.asm.java5.repository.CategoryRepository;
import com.asm.java5.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CookieService cookieService;
    @Autowired
    SessionService sessionService;
    @Autowired
    CustomerRepository customerRepository;

    @ModelAttribute("categories")
    public List<Category> getCategoriesUpAll() {
        return categoryRepository.findAll();
    }
    @ModelAttribute("user")
    public Customer getLoginUpAll(){
        var email = cookieService.get(SessionAttr.CUSTOMER);
        if (email == null){
            return null;
        }
        Customer customer = customerRepository.findByEmail(email.getValue());
        sessionService.set(SessionAttr.CUSTOMER,customer);
        return customer;
    }
}
