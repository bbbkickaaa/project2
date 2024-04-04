package com.API.Notice.DTO;

import com.API.Board.Entity.BoardCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReviewDTO {

	private Long noticeId;
    private String title;
    private Long userId;
    private String nickname;
    private int views;
    private int likes;
    private Long ImageCount;
}