package com.API.Board.DTO;

import java.util.HashSet;
import java.util.Set;

import com.API.Board.Entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardReviewDTO {

	private Long boardId;
    private String title;
    private Long userId;
    private String nickname;
    private int views;
    private int likes;
    private Long commentCount;
}
