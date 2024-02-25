package com.API.Board.Entity;


import java.time.LocalDate;

import com.API.User.Entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Comment {

    @Column(length = 100)
    private String content;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;
    
    @Column(name="write_date" ,nullable = false,length = 8)
	private String writeDate = LocalDate.now().toString().replace("-", "");

}
