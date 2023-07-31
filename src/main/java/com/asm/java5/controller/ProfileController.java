package com.asm.java5.controller;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.domain.Product;
import com.asm.java5.repository.CustomerRepository;
import com.asm.java5.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    SessionService sessionService;
    @Autowired
    CustomerRepository customerRepository;

    @ModelAttribute("customer")
    public Customer getCustomer(){
        Customer customer = sessionService.get(SessionAttr.CUSTOMER);
        return customer;
    }

    @GetMapping("")
    public String profile(){
        return "profile";
    }

    @PostMapping("change-pass")
    public String changePass(Model model, @RequestParam("password") String password,
                             @RequestParam("newpassword") String newpass,
                             @RequestParam("renewpassword") String renewpass){
        Customer customer = customerRepository.findById(getCustomer().getCustomerId()).get();
        try{
            if(!(customer.getPassword().equals(password))){
                model.addAttribute("messagePass", "*Mật khẩu cũ không chính xác");
            } else if (!newpass.equals(renewpass)){
                model.addAttribute("messagePass", "*Xác nhật mật khẩu không chính xác");
            } else {
                customer.setPassword(newpass);
                customerRepository.save(customer);
                model.addAttribute("messagePass", "*Thay đổi thành công");
            }
        } catch (Exception e){
            model.addAttribute("messagePass", "*Vui lòng điền đầy đủ thông tin");
        }
        model.addAttribute("tab", "changePassword");
        return "forward:/profile";
    }

    @PostMapping("change-profile")
    public String changeProfile(Model model, @RequestParam("name") String name,
                                @RequestParam("phone") String phone,
                                @RequestParam("email") String email){
        Customer customer = sessionService.get(SessionAttr.CUSTOMER);
        if(!(name.equals("") || phone.equals("") || email.equals(""))){
            if(customerRepository.findById(customer.getCustomerId()).get() != null) {
                customer.setName(name);
                customer.setPhone(phone);
                customer.setEmail(email);
                customerRepository.save(customer);
                model.addAttribute("messageProfile", "*Thay đổi thành công");
            } else {
                model.addAttribute("messageProfile", "*Không tìm thấy tài khoản của bạn");
            }
        } else {
                model.addAttribute("messageProfile", "*Vui lòng điền đầy đủ thông tin");
        }
        model.addAttribute("tab", "changeProfile");
        return "forward:/profile";
    }
}
