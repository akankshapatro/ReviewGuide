package com.user.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.user.entity.User;
import com.user.exception.AgreementNotAcceptedException;
import com.user.helper.EmailDetails;
import com.user.helper.Message;
import com.user.service.EmailService;
import com.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;

	@Value("${spring.mail.username}")
	private String sender;
	
	/*Method Handler to load home page*/
	@RequestMapping("/home")
	public String home(Model model) {
		log.info("Loading Home Page...");
		model.addAttribute("title", "Home-Review Guide");
		return "home";
		}
	
	/*Method Handler to load About page*/
	@RequestMapping("/about")
	public String about(Model model) {
		log.info("Loading About Page...");
		model.addAttribute("title", "About-Review Guide");
		return "about";
		}
	
	/*Method Handler to load Signup page*/
	@RequestMapping("/signup")
	public String signup(Model model) {
		log.info("Loading Signup Page...");
		model.addAttribute("title", "Signup-Review Guide");
		model.addAttribute("user",new User());
		return "signup";
		}
	
	/*Method Handler to load Login page*/
	@RequestMapping("/login")
	public String login(Model model) {
		log.info("Loading Login Page...");
		model.addAttribute("title", "Login-Review Guide");
		model.addAttribute("user",new User());
		return "login";
		}
	
	/*Method Handler to handle User Registration
	 * 
	 * Takes 'agreement' field to validate if the Terms and Conditions checkbox is checked or not
	 * 
	 * */
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public String register(@ModelAttribute("user") @Valid User user,BindingResult bresult,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model,HttpSession session) {
		try {
			log.info("Entered User Registration of UserController.");
			log.info("User date for Registration: "+user.toString());
			log.info("Agreement accepted: "+agreement);
			if(!agreement)
			{
				log.info("Terms and Conditions not accepted");
				throw new AgreementNotAcceptedException("Please accept the terms and conditions to proceed further");
			}
			
			if(bresult.hasErrors()) {
				log.info("Validation Failed !!");
				model.addAttribute("user",user);
				return "signup";
			}
			
			userService.checkValidation(user);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			log.info("Encoded Password: "+user.getPassword());
			
			user.setRole("ROLE_USER");
			user.setActive(true);
			User result = this.userService.addUser(user);
			model.addAttribute("user",new User());
			session.setAttribute("message", new Message("Successfully Registered","alert-success"));
			
			//to send email for successfull registration
			log.info("START... Sending Email to New User");
			EmailDetails details =  new EmailDetails();
			details.setSender(sender);
			details.setSubject("Welcome to ReviewGuide !!");
			details.setRecipient(user.getEmail());
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("name", user.getName());
			properties.put("location", "India");
			properties.put("sign", "ReviewGuide Team");
	        details.setProperties(properties);
	        
	        emailService.sendMail(details);
	        log.info("END... Email sent successfully");
			return "signup";
			
		} catch (Exception e) {
			log.info("Error occurred during registration");
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something went wrong. "+e.getMessage(),"alert-danger"));
			return "signup";
			
		}
	}
	

}
