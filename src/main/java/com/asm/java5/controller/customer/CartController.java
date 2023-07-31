package com.asm.java5.controller.customer;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.*;
import com.asm.java5.repository.OrderDetailRepository;
import com.asm.java5.repository.OrderRepository;
import com.asm.java5.repository.ProductRepository;
import com.asm.java5.service.MailerService;
import com.asm.java5.service.SessionService;
import com.asm.java5.service.StorageService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    SessionService sessionService;
    @Autowired
    StorageService storageService;
    @Autowired
    MailerService mailerService;


    @ModelAttribute("orderDetails")
    public List<OrderDetail> getProducts(){
        Customer customer = sessionService.get(SessionAttr.CUSTOMER);
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByCustomer(customer);
        return orderDetails;
    }

    @RequestMapping("")
    public String list(Model model){
        List<OrderDetail> orderDetails = getProducts();
        if (orderDetails.size() > 0){
            double total = 0;
            for (OrderDetail item : orderDetails){
                total += item.getUnitPrice() * item.getQuantity();
            }
            model.addAttribute("total", total);
        }
        return "product/cart";
    }

    @PostMapping("update")
    public String update(@RequestParam("id") Integer id, @RequestParam("quantity") Integer quantity){
        OrderDetail orderDetail = orderDetailRepository.findById(id).get();
        orderDetail.setQuantity(quantity);
        orderDetailRepository.save(orderDetail);
        return "redirect:/cart";
    }

    @RequestMapping("add")
    public String add(@RequestParam("id") Integer id){
        Customer customer = sessionService.get(SessionAttr.CUSTOMER);
        Order order = orderRepository.findByCustomerAndStatus(customer,0);
        // kiểm tra xem người dùng đã có cart chưa thanh toán không
        if(order == null){
            order = new Order();
            order.setCustomer(customer);
            order.setOrderDate(new Date());
            order.setStatus((short) 0);
            orderRepository.save(order);
        }
        // Lấy thông tin của sản phẩm
        Product product = productRepository.findById(id).get();
        // Tìm kiếm nếu đã có trong giỏ hàng trước đó thì tăng số lượng lên
        OrderDetail orderDetail = orderDetailRepository.findByOrderAndProduct(order, product);
        if(orderDetail != null){
            orderDetail.setQuantity(orderDetail.getQuantity() + 1);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng thì tạo mới
            orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            // Xem thử sản phẩm có được giảm giá không
            orderDetail.setUnitPrice(product.getUnitPrice() / 100 * (100 - product.getDiscount()));
            orderDetail.setQuantity(1);
        }
        orderDetailRepository.save(orderDetail);
        return "redirect:/cart";
    }

    @RequestMapping("delete")
    public String delete(@RequestParam("id") Integer id){
        orderDetailRepository.deleteById(id);
        return "redirect:/cart";
    }

    @RequestMapping("done")
    public String done(@RequestParam("orderId") Integer id) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepository.findById(id).get();
        order.setStatus((short) 1);
        Customer customer = sessionService.get(SessionAttr.CUSTOMER);
        orderRepository.save(order);
        mailerService.send(customer.getEmail(), SessionAttr.SUBJECT_BUY_PRODUCT,
                "Chào " + customer.getName() + ", " + SessionAttr.BODY_BUY_PRODUCT);
        return "redirect:/history";
    }

    @GetMapping("/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
