package com.asm.java5.service;

import com.asm.java5.constant.SessionAttr;
import com.asm.java5.domain.Customer;
import com.asm.java5.repository.CategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Service 
public class GlobalInterceptor implements HandlerInterceptor {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SessionService session;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        // lấy từ session
        Customer user = (Customer) session.get(SessionAttr.CUSTOMER);
        if(user == null) {
            // chưa đăng nhập
            session.set(SessionAttr.URIBEFORE, uri);
            response.sendRedirect("/login");
            return false;
        }
        // không đúng vai trò
        else if(user.getStatus() == 0 && uri.startsWith("/admin/")) {
            response.sendRedirect("/customer/error");
            return false;
        }
        else if(user.getStatus() == 1 && uri.startsWith("/cart") || user.getStatus() == 1 && uri.startsWith("/history")) {
            response.sendRedirect("/customer/error");
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res,
                           Object handler, ModelAndView mv) throws Exception {
//        req.setAttribute("categories", categoryRepository.findAll());
////        Tìm kiếm sự tồn tại khi login
//        Customer customer = session.get(SessionAttr.CUSTOMER);
//        if(customer != null){
//            req.setAttribute("user", customer);
//        }
    }
}