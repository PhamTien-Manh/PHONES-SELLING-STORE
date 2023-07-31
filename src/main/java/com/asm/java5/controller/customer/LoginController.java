package com.asm.java5.controller.customer;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.repository.CustomerRepository;
import com.asm.java5.service.CookieService;
import com.asm.java5.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SessionService session;
    @Autowired
    HttpServletResponse response;
    @Autowired
    CookieService cookieService;
    @Autowired
    HttpServletRequest request;

    @GetMapping("/login")
    public String send(Model model){
        var email = cookieService.get(request,"email");
        var pass = cookieService.get(request, "pass");
        if (email != null && pass != null){
            model.addAttribute("email", email.getValue());
            model.addAttribute("password", pass.getValue());
            model.addAttribute("remember", true);
        }
        else {
            model.addAttribute("remember", false);
        }
        return "pages/pages-login";
    }

    @PostMapping("/login")
    public String login(Model model,@RequestParam("email") String email,
                        @RequestParam("password") String pass,
                        @RequestParam(value = "remember", required = false) boolean remember){
        try {
            Customer customer = customerRepository.findByEmail(email);
            if(!customer.getPassword().equals(pass)){
                // xử lý sai mật khẩu
            } else {
                if(remember){
                    cookieService.add("email", email, 30, response);
                    cookieService.add("pass", pass, 30, response);
                } else {
                    cookieService.remove("email", request, response);
                    cookieService.remove("pass", request, response);
                }
                session.set(SessionAttr.CUSTOMER,customer);
                return "redirect:/index";
            }
        } catch (Exception e){
            // xử lý sai tài khoản
        }
        return "redirect:/login";
    }
}
