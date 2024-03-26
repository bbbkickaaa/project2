package com.API.Report.Entity;

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
@Table(name = "report")
public class Report {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String category;
	
	String content;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
	User userId;
	
	  @Column(name = "write_date", length = 14)
	private String writeDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	
	
}
