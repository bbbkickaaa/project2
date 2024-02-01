package com.API.User.Oauth2;

import java.io.Serializable;

import com.API.User.Entity.User;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable {

	private String name;
	private String email;
	private String picture;
	
	public SessionUser(User user) {
		this.name = user.getUserid();
		this.email = user.getEmail();
		this.picture = user.getPicture();
	}
}
