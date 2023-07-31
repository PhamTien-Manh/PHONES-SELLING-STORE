package com.asm.java5.controller.customer;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.domain.OrderAdmin;
import com.asm.java5.domain.OrderDetail;
import com.asm.java5.repository.OrderDetailRepository;
import com.asm.java5.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    SessionService sessionService;

    @ModelAttribute("orderAdmins")
    public List<OrderAdmin> getOrder(){
        Customer customer = sessionService.get(SessionAttr.CUSTOMER);
        List<OrderAdmin> orderAdmins = orderDetailRepository.findOrderAdminWithCustomer(customer);
        return orderAdmins;
    }

    @GetMapping("history")
    public String history(){
        return "product/history";
    }

    @GetMapping("history/view")
    public String view(@RequestParam("orderId") Integer orderId, Model model){
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderOrderId(orderId);
        double total = 0;
        for (OrderDetail item : orderDetails){
            total += item.getUnitPrice() * item.getQuantity();
        }
        model.addAttribute("total", total);
        model.addAttribute("orderDetails", orderDetails);
        return "forward:/history";
    }
}
