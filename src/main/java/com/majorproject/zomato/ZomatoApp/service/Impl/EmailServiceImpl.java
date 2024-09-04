package com.majorproject.zomato.ZomatoApp.service.Impl;

import com.majorproject.zomato.ZomatoApp.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Override
    public void sendOrderDetailsToPartner(String[] toEmail, String subject, String templateName, Map<String, Object> modelName) throws MessagingException {

        //create MimeMessage to send email
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        //crete context of thymeleaf
        Context context = new Context();
        context.setVariables(modelName);
        String httpContent = templateEngine.process(templateName , context);

        //create the message
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(httpContent , true);

        //send the email
        javaMailSender.send(message);



    }

    @Override
    public void sendOrderDetailsToPartner(String toEmail, String subject, String templateName, Map<String, Object> templateModel) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        //set up context for thymeleaf
        Context context = new Context();
        context.setVariables(templateModel);
        String httpContent = templateEngine.process(templateName , context);

        //set up the mail
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(httpContent , true);

        javaMailSender.send(message);
    }
}
