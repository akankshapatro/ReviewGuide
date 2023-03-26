package com.user.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.user.config.ThymleafTemplateConfig;
import com.user.helper.EmailDetails;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
	
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	/* Method to send a welcome email */
	public void sendMail(EmailDetails details) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(details.getProperties());
        String html = templateEngine.process("welcome-email.html", context);
        log.info("html"+html);
        helper.setTo(details.getRecipient());
        helper.setText(html, true);
        helper.setSubject(details.getSubject());
        helper.setFrom(details.getSender());
        emailSender.send(message);
        
	}
	
	

}
