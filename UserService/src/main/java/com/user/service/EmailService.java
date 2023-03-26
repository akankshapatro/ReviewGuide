package com.user.service;

import javax.mail.MessagingException;

import com.user.helper.EmailDetails;

public interface EmailService {
	
	
	void sendMail(EmailDetails details) throws MessagingException;

}
