package com.API.User.DTO;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TitleAndCategoryDTO {

	Long id;
	String title;
	String category1;
	String category2;
	String category3;
	String date;
	int view;
}
