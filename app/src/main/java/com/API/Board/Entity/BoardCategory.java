package com.API.Board.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class BoardCategory {
	
	@Column(nullable = false)
	String category1;
	@Column(nullable = false)
	String category2;
	@Column(nullable = false)
	String category3;
}
