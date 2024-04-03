package com.API.Notice.DTO;

import java.util.List;
import java.util.Set;

import com.API.Board.Entity.BoardCategory;
import com.API.Board.Entity.BoardImage;
import com.API.Notice.Entity.NoticeImage;
import com.API.User.Entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeIdWithImgDTO {
	
	private Long id;
	private List<NoticeImage> noticeImage;

}
