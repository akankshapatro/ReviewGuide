package com.user.exception;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.user.entity.User;
import com.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@Autowired
	UserService userService;

	@ExceptionHandler(value = RuntimeException.class)
	public String handleException(RuntimeException ex,Principal principal,Model model) {
		log.info("Entered exception handler");
		String username = principal.getName();
		User user = userService.getUserByEmail(username);
		model.addAttribute("user",user);
		return "error";
	}
}
