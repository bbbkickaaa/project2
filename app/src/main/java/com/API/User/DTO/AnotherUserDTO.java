package com.API.User.DTO;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnotherUserDTO {

	Long Id;
	String nickname;
	String picture;
	String createdDate;
    List<TitleAndCategoryDTO> dto;
}
