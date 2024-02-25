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
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private int likes;
    private int views;
    private Long userIdx;
    private String nickname;
    private String writeDate;
    private List<Comment> comments;

}
