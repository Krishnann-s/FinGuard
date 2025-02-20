package com.main.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.main.dto.EmailRequest;


@Service
public class NotificationService 
{
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmailId;
	
	public void sendEmail(EmailRequest emailRequest)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom(fromEmailId);
		message.setTo(emailRequest.getToEmail());
		message.setSubject(emailRequest.getSubject());
		message.setText(emailRequest.getBody());
		
		javaMailSender.send(message);
		 logger.info("Account created mail sent to {}" ,emailRequest.getToEmail());	
	}

	
}
