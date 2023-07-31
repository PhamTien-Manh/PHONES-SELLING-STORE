package com.asm.java5.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Service
public class CookieService {
    /**
     * Đọc cookie từ request
     * @param name tên cookie cần đọc
     * @return đối tượng cookie đọc được hoặc null nếu không tồn tại
     */
    public Cookie get(HttpServletRequest request, String name) {
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
    public Cookie add(String name, String value, int days, HttpServletResponse response) {
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
    public void remove(String name, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(name)) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    response.addCookie(cookie);
                }
            }
        }
    }

}
