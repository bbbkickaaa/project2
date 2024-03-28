package com.API.Board.DTO;

import java.util.List;
import java.util.Set;

import com.API.Board.Entity.BoardCategory;
import com.API.Board.Entity.BoardImage;
import com.API.User.Entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardIdWithImgDTO {
	
	private Long id;
	private List<BoardImage> boardImage;

}
