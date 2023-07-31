package com.asm.java5.controller.admin;


import com.asm.java5.domain.OrderAdmin;
import com.asm.java5.domain.OrderDetail;
import com.asm.java5.repository.OrderDetailRepository;
import com.asm.java5.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin/orders")
public class OrderController {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @ModelAttribute("orderAdmins")
    public List<OrderAdmin> getOrder(){
        List<OrderAdmin> orderAdmins = orderDetailRepository.findOrderAdminWithStatus();
        return orderAdmins;
    }
    @RequestMapping("")
    public String list(@ModelAttribute("id") String id){
        return "admin/manager-order";
    }
    @GetMapping("delete")
    public String delete(@RequestParam("id") Integer id, Model model){
        orderDetailRepository.deleteOrderDetailByOrderOrderId(id);
        orderRepository.deleteById(id);
        model.addAttribute("message", "Xóa thành công!");
        return "forward:/admin/orders";
    }
    @GetMapping("view")
    public String view(@RequestParam("id") Integer id, Model model){
       List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderOrderId(id);
       model.addAttribute("orderDetails", orderDetails);
       model.addAttribute("id", id);
        return "forward:/admin/orders";
    }
}


