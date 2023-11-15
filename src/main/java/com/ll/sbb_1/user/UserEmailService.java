package com.ll.sbb_1.user;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserEmailService {

    private final JavaMailSender javaMailSender;
    public void mailSend(String email, String type, String code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(type);
        simpleMailMessage.setText(code);

        this.javaMailSender.send(simpleMailMessage);
    }
}