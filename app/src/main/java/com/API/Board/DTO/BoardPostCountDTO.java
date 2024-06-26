package com.API.Board.DTO;

import java.util.List;

import com.API.Board.Entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostCountDTO {

	Long postCount;
	Long commentCount;
}
