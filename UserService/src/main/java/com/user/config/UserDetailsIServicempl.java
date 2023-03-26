package com.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.user.dao.UserRepository;
import com.user.entity.User;

public class UserDetailsIServicempl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		UserDetails userDetails = new CustomUserDetails(user);
		
		return userDetails;
	}

}
