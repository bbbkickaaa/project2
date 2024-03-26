package com.API.Report.DTO;

import com.API.Board.Entity.BoardCategory;
import com.API.User.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
	
	Long boardId;
	String type;
	String content;
	Long userId;
	String nickname;
	String writeDay;
}
