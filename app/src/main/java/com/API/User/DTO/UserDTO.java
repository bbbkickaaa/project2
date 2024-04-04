package com.API.User.DTO;

import com.API.User.Entity.User;
import com.API.User.Entity.UserLevel;
import com.API.User.Entity.UserRole;

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
	private String picture;
	private String userid;
	private String nickname;
	private UserLevel userLevel;
	private String email;
	private String createdDate;
	private UserRole role;
	
	public static UserDTO fromUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setPicture(user.getPicture());
        dto.setUserid(user.getUserid());
        dto.setRole(user.getRole());
        dto.setUserLevel(user.getUserLevel());
        return dto;
    }
}
