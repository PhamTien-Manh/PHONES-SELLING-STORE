package com.asm.java5.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {
    String from;
    String to;
    String[] cc;
    String[] bcc;
    String subject;
    String body;
    public MailInfo(String to, String subject, String body) {
        this.from = "manhptpd05941@fpt.edu.vn";
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}
