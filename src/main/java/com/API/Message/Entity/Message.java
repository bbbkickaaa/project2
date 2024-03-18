package com.API.Message.Entity;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.API.User.Entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "forward_id")
    private User forwardId;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receive_id")
    private User receiveId;
	
	@Column(nullable = false,length = 200)
	String content;
	
	@Column(nullable = false)
	Boolean isRead = false;
	
	@Column(name = "write_date", length = 14)
    private String writeDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	
	
	
	
}
