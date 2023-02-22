package ir.maktab.finalprojectphase4.service.impl;

import ir.maktab.finalprojectphase4.exception.SendEmailFailedException;
import ir.maktab.finalprojectphase4.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    @Override
    public void sendEmail(String to, String emailMessage) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailMessage, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("reihaneh763@gmail.com");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new SendEmailFailedException("Failed to send email for: " + emailMessage);
        }
    }

    @Override
    public SimpleMailMessage createEmail(String toEmail, String confirmationToken, String accountType) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setFrom("reihaneh763@gmail.com");
        if (accountType.equals("customer")) {
            mailMessage.setSubject("Complete Customer Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/signup/confirm-customer/" + confirmationToken);
        } else if (accountType.equals("expert")) {
            mailMessage.setSubject("Complete Expert Registration!");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/signup/confirm-expert/" + confirmationToken);
        }
        return mailMessage;
    }
}

