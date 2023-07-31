package com.asm.java5.service.impl;

import com.asm.java5.domain.MailInfo;
import com.asm.java5.service.MailerService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;


@Service
public class MailerServiceImpl implements MailerService {

    @Autowired
    JavaMailSender sender;

    @Override
    public void send(MailInfo mail) throws MessagingException, UnsupportedEncodingException {
        // Tạo message
        MimeMessage message = sender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(new InternetAddress("manhptpd05941@fpt.edu.vn", "TmpShop", "UTF-8"));
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody(), true);
            helper.setReplyTo("manhptpd05941@fpt.edu.vn");
            String[] cc = mail.getCc();

            if(cc != null && cc.length > 0) {
                helper.setCc(cc);
            }
            String[] bcc = mail.getBcc();
            if(bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
            }
        // Gửi message đến SMTP server
        sender.send(message);
    }

    @Override
    public void send(String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        this.send(new MailInfo(to, subject, body));
    }

}
