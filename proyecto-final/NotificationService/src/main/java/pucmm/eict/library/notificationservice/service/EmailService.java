package pucmm.eict.library.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pucmm.eict.library.notificationservice.model.Email;

import java.io.File;

@Service
public class EmailService {

    public String sendSimpleMail(Email email) {
        return "Mail Sent Successfully...";
    }

    public String sendMailWithAttachment(Email email) {
        return "Mail Sent Successfully...";
    }
}
