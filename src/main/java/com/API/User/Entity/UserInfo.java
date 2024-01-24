package com.API.User.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

	@Column(table="user_info")
	private long number;
	
	@Column(table="user_info")
	private long birth;
	
	@Column(table="user_info")
	private String email;
	
	@Column(table="user_info")
	private String gender; 
}