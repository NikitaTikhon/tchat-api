package com.moro.tchat.event;

import com.moro.tchat.entity.User;
import com.moro.tchat.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private JavaMailSender javaMailSender;

    private TemplateEngine templateEngine;

    private AuthService authService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        authService.createVerificationToken(user, token);

        final MimeMessage email;
        try {
            email = constructEmailMessage(event, user, token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(email);
    }

    private MimeMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) throws MessagingException {
        final String recipientAddress = user.getEmail();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = "http://" + event.getAppUrl() + "/api/v1/registrationConfirm?token=" + token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setTo(recipientAddress);
        helper.setFrom("project_minesup@mail.ru");
        helper.setSubject(subject);

        Context context = new Context();
        context.setVariable("link", confirmationUrl);
        String htmlContent = templateEngine.process("email-template", context);
        helper.setText(htmlContent, true);

        return mimeMessage;
    }

}
