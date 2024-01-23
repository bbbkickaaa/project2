package com.API.User;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import com.API.User.Entity.User;

@Service
public class NewUserServiceImpl implements NewUserService {
	
	 @Autowired
	    private UserRepository userRepository;
	 
	    public User createUser(User user) {
	        return userRepository.save(user);
	    }


}
