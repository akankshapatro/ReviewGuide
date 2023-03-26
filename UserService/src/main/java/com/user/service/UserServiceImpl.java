package com.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.dao.UserRepository;
import com.user.entity.User;
import com.user.exception.UserAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	/* This method is used to add a new user */
	public User addUser(User user) {
		log.info("Entered into addUser method of UserServiceImpl ");
		return userRepository.save(user);
	}

	/* This method is used get user based on its username ie email */
	public User getUserByEmail(String username) {
		log.info("Entered into getUserByEmail method of UserServiceImpl ");
		return userRepository.getUserByEmail(username);
	}

	/* This method is used to validate if user account already exists */
	public void checkValidation(User user) throws Exception {
		log.info("Entered into checkValidation method of UserServiceImpl ");
		User oldUser = getUserByEmail(user.getEmail());
		if(oldUser!=null) {
			log.info("User Account already Exists with username: "+oldUser.getEmail());
			throw new UserAlreadyExistsException("Account Already Exists");
		}
		
	}

	public String getNameById(Long id) {
		log.info("Entered into getNameById method of UserServiceImpl");
		User user = userRepository.findById(id).orElse(null);
		return user.getName();
	}

}
