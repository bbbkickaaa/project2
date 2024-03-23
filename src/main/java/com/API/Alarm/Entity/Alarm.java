package com.API.Alarm.Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import com.API.Board.Entity.Board;
import com.API.Board.Entity.BoardCategory;
import com.API.Board.Entity.BoardImage;
import com.API.Board.Entity.Comment;
import com.API.User.Entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long referencedId; // 참조되는 게시글이나 메시지의 ID
    
    @Column(nullable = false)
    private Long recipientId; // 알람을 받는 사용자의 ID
    
    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    private String forwardNickname;
    
    @Column(name="time" ,nullable = false,length = 14)
    private String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    
    @Column(nullable = false)
    private Long forwarduserId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType type;

    @Column(nullable = false)
    private boolean view = false;

    @Column(nullable = true)
    private String url;

	public enum AlarmType {
	    COMMENT,
	    MESSAGE,
	    OTHER // 필요에 따라 다른 타입 추가
		}
}
