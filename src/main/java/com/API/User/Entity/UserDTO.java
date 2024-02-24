package com.API.User.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {

	private Long id;
	private String userid;
	private String nickname;
	private UserLevel userLevel;
	private String email;
	private String createdDate;
}
