package com.asm.java5.service;

import com.asm.java5.constant.SessionAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Service
public class CookieService {
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpServletRequest request;
    /**
     * Đọc cookie từ request
     * @param name tên cookie cần đọc
     * @return đối tượng cookie đọc được hoặc null nếu không tồn tại
     */
    public Cookie get(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    /**
     * Đọc giá trị của cookie từ request
//     * @param name tên cookie cần đọc
     * @return chuỗi giá trị đọc được hoặc rỗng nếu không tồn tại
     */
    public String getCookie(@CookieValue(value = "user",
            defaultValue = "No color found in cookie") String color) {
        return color;
    }
    /**
     * Tạo và gửi cookie về client
     * @param name tên cookie
     * @param value giá trị cookie
//     * @param days thời hạn (ngày)
     * @return đối tượng cookie đã tạo
     */
    public Cookie add(String name, String value, int days) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(days * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return cookie;
    }
    /**
     * Xóa cookie khỏi client
     * @param name tên cookie cần xóa
     */
    public void remove(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void exception(String error, HttpServletRequest request, HttpServletResponse response) throws IOException {
        remove(SessionAttr.CUSTOMER);
        request.getSession().setAttribute("loginError", error);
        response.sendRedirect("/login");
    }
}
