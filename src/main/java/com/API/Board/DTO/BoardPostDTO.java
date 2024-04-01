package com.API.Board.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostDTO {

	Long id;
	Long userIdx;
	String content;
	String title;
	String category1;
	String category2;
	String category3;
}
