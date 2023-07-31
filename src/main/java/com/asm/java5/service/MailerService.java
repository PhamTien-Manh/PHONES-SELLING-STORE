package com.asm.java5.service;

import com.asm.java5.domain.MailInfo;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


@Service
public interface MailerService {
    /**
     * Gửi email
     * @param mail thông tin email
     * @throwss MessagingException lỗi gửi email
     */
    void send(MailInfo mail) throws MessagingException, UnsupportedEncodingException;
    /**
     * Gửi email đơn giản
     * @param to email người nhận
     * @param subject tiêu đề email
     * @param body nội dung email
     * @throwss MessagingException lỗi gửi email
     */
    void send(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException;

}
