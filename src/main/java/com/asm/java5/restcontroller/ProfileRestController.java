package com.asm.java5.restcontroller;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.repository.CustomerRepository;
import com.asm.java5.service.CookieService;
import com.asm.java5.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class ProfileRestController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CookieService cookieService;

    @Autowired
    HttpServletRequest request;

//    @GetMapping("/rest/profile")
//    public Customer getProfile(){
//        return customer;
//    }
}
