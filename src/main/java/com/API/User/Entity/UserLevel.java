package com.API.User.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class UserLevel {

	@Column(table = "user_level")
	 private int level;
	
	@Column(table = "user_level")
	 private int points;
	
	 public void setLevel(int level) {
	        this.level = level;
	    }
	 
	 public void setPoints(int points) {
	        this.points = points;
	        updateLevel(); // 포인트가 설정될 때마다 레벨 업데이트
	    }
	 
	 private void updateLevel() {
	        if (this.points >= 500) {
	            this.level = 5;
	        } else if (this.points >= 200) {
	            this.level = 4;
	        } else if (this.points >= 50) {
	            this.level = 3;
	        } else if (this.points >= 10) {
	            this.level = 2;
	        } else {
	            this.level = 1;
	        }
	    }

	public UserLevel(int i, int j) {
		this.level = i;
		this.points = j;
	}
}
