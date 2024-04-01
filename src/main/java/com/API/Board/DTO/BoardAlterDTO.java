package com.API.Board.DTO;

import java.util.List;

import com.API.Board.Entity.BoardCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardAlterDTO {

	private Long id;
	private String title;
    private String content;
    private Long userIdx;
    private String category1;
    private String category2;
    private String category3;
}
