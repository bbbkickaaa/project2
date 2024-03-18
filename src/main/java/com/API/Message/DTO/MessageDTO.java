package com.API.Message.DTO;

import java.util.List;
import java.util.Set;

import com.API.Board.DTO.CommentDTO;
import com.API.Board.Entity.BoardCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

	Long forwardId;
	Long receiveId;
	String content;
}
