package com.API.User.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.API.User.UserRepository;
import com.API.User.Entity.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	 @Autowired
	    private UserRepository userRepository; // 사용자 정보를 관리하는 레포지토리

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Optional<User> user = userRepository.findByUserid(username);
	        if (user.isEmpty()) {
	            throw new UsernameNotFoundException("User not found with username: " + username);
	        }
	        return new CustomUserDetails(user.get());
	    }
	}