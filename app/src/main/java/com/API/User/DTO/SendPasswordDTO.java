package com.API.User.DTO;

import com.API.User.Entity.UserLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendPasswordDTO {

	String userId;
	String email;
}
