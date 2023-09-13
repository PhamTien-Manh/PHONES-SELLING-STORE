package com.asm.java5.controller.customer;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.repository.CustomerRepository;
import com.asm.java5.service.MailerService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Controller
public class RegisterController {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MailerService mailerService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String send(@ModelAttribute("customer")Customer customer){
        return "pages/pages-register";
    }
    @PostMapping("/register")
    public String register(@ModelAttribute("customer") Customer customer) throws MessagingException, UnsupportedEncodingException {
        customer.setRegisteredDate(new Date());
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        mailerService.send(customer.getEmail(), SessionAttr.SUBJECT_REGISTER,
                "Chào " + customer.getName() + ", " + SessionAttr.BODY_REGISTER);
        return "redirect:/login";
    }
}
