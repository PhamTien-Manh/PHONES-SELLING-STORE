package com.asm.java5.controller.customer;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.repository.CustomerRepository;
import com.asm.java5.service.MailerService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ForgotPassController {
    @Autowired
    MailerService mailerService;
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("forgot-pass")
    public String forgotPass(){
        return "pages/pages-forgot-pass";
    }

    @PostMapping("forgot-pass")
    public String send(Model model, @RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
        Customer customer = customerRepository.findByEmail(email);
        if(customer == null){
            model.addAttribute("message", "Email của bạn không chính xác");
            return "pages/pages-forgot-pass";
        }
        int randomNumber = ThreadLocalRandom.current().nextInt(1000, 10000);
        customer.setPassword(String.valueOf(randomNumber));
        customerRepository.save(customer);
        mailerService.send(customer.getEmail(), SessionAttr.SUBJECT_FORGOT_PASS,
                "Chào " + customer.getName() + ", " + SessionAttr.BODY_FORGOT_PASS + customer.getPassword());
        model.addAttribute("message", "Hãy kiểm tra mail của bạn");
        return "pages/pages-login";
    }
}
