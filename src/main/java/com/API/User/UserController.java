package com.API.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.User.Entity.User;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	NewUserService newUserService;

	@PostMapping("/save")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return newUserService.createUser(user);
    }
}
