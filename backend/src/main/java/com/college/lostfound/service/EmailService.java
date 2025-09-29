package com.college.lostfound.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender){ this.mailSender = mailSender; }

    public void sendSimple(String to, String subject, String text){
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(text);
            mailSender.send(msg);
        } catch (Exception ex){
            // log error — in production use proper logging
            System.err.println("Failed to send email: " + ex.getMessage());
        }
    }
}
