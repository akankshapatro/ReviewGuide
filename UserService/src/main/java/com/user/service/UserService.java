package com.user.service;

import com.user.entity.User;

public interface UserService {
	
	User addUser(User user);
	
	User getUserByEmail(String username);
	
	void checkValidation(User user) throws Exception;
	
	String getNameById(Long id);

}
