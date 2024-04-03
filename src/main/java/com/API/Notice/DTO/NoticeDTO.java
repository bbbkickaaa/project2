package com.API.Notice.DTO;

import java.util.List;
import java.util.Set;

import com.API.Board.Entity.Board;
import com.API.Board.Entity.BoardCategory;
import com.API.Notice.Entity.Notice;
import com.API.User.DTO.UserDTO;
import com.API.User.Entity.User;
import com.API.User.Entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {

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
    private UserRole role;
    private Set<Integer> likesUser;
    private List<String> imageUrls;
    private boolean isFavorite;
    
    public static NoticeDTO fromNotice(Notice notice) {
    	NoticeDTO noticeDTO = new NoticeDTO();
    	noticeDTO.setId(notice.getId());
    	noticeDTO.setTitle(notice.getTitle());
    	noticeDTO.setContent(notice.getContent());
    	noticeDTO.setUserIdx(notice.getAuthor().getId());
    	noticeDTO.setLikes(notice.getLikes());
    	noticeDTO.setViews(notice.getViews());
    	noticeDTO.setFavorite(false);
    	noticeDTO.setWriteDate(notice.getWriteDate());
    	noticeDTO.setAlterDate(notice.getAlterDate());
    	noticeDTO.setPicture(notice.getAuthor().getPicture());
    	noticeDTO.setLevel(notice.getAuthor().getUserLevel().getLevel());
    	noticeDTO.setLikesUser(notice.getLikesUsers());
        return noticeDTO;
    }

}
