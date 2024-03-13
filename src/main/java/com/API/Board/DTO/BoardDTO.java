package com.API.Board.DTO;

import java.util.List;
import java.util.Set;
import com.API.Board.Entity.BoardCategory;
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
    private int level;
    private int likes;
    private int views;
    private Long userIdx;
    private String nickname;
    private String writeDate;
    private String alterDate;
    private String picture;
    private BoardCategory category;
    private Set<Integer> likesUser;
    private List<CommentDTO> comments;
    private List<String> imageUrls;

}
