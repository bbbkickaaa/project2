package com.API.Notice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticePostDTO {

	Long userIdx;
	Long noticeId;
	String content;
	String title;
}