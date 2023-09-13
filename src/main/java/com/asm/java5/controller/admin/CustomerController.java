package com.asm.java5.controller.admin;

import com.asm.java5.domain.Category;
import com.asm.java5.domain.Customer;
import com.asm.java5.enums.Role;
import com.asm.java5.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/customers")
public class CustomerController {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PasswordEncoder encoder;
//    Để readonly true or false thẻ password
    @ModelAttribute("customers")
    public Page<Customer> getAll(@RequestParam("page") Optional<Integer> page
            , @RequestParam("keyword") Optional<String> keyword, Model model){
        Pageable pageable = PageRequest.of(page.orElse(0), 4);
        Page<Customer> customers = customerRepository.findAllByNameLike("%" + keyword.orElse("") + "%",pageable);
        model.addAttribute("keyword", keyword.orElse(""));
        return customers;
    }

    @RequestMapping("")
    public String list(Model model){
        Customer customer = new Customer();
        customer.setRole(Role.CUSTOMER);
        model.addAttribute("customer", customer);
        model.addAttribute("edit", false);
        return "admin/manager-customer";
    }
    @GetMapping("delete")
    public String delete(@RequestParam("id") Integer id, Model model){
        customerRepository.deleteById(id);
        model.addAttribute("message", "Xóa thành công!");
        return "forward:/admin/customers";
    }
    @GetMapping("edit")
    public String edit(Model model, @RequestParam("id") Integer id){
        Customer customer = customerRepository.findById(id).get();
        model.addAttribute("customer", customer);
        model.addAttribute("edit",true);
        return "admin/manager-customer";
    }
    @PostMapping("update-or-create")
    public String save(Model model, @Valid @ModelAttribute("customer") Customer customer,
                       BindingResult result){
        if(result.hasErrors()){
            return "admin/manager-customer";
        }
        Customer customerNew = customerRepository.findById(customer.getCustomerId()).get();
        if (customerNew == null){
            customer.setPassword(encoder.encode(customer.getPassword()));
            customer.setRegisteredDate(new Date());
        }
        customerRepository.save(customer);
        model.addAttribute("message", "Lưu thành công!");
        model.addAttribute("edit", false);
        return "forward:/admin/customers";
    }
    @PostMapping("search")
    public String search(Model model, @RequestParam("keyword") Optional<String> keyword,
                         @RequestParam("page") Optional<Integer> p){
        Pageable pageable = PageRequest.of(p.orElse(0), 4);
        Page<Customer> customers  = customerRepository.findAllByNameLike("%" + keyword.orElse("") + "%", pageable);
        model.addAttribute("keyword", keyword.orElse(""));
        model.addAttribute("customer", new Customer());
        model.addAttribute("customers", customers);
        return "admin/manager-customer";
    }
}
