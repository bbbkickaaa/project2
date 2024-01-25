package com.API.User;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.User.Entity.User;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	NewUserService newUserService;
	
	@Autowired
	LoginService loginService;

	@PostMapping("/save")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return newUserService.createUser(user);
    }
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody User user){
		return loginService.loginUser(user);
	}
}
