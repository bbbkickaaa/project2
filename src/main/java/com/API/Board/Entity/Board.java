package com.API.Board.Entity;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.API.User.Entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "board")
@SecondaryTable(name = "board_comments", pkJoinColumns = @PrimaryKeyJoinColumn(name = "board_id"))
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private int likes = 0;

    @Column(nullable = false)
    private int views = 0;
    
    @ElementCollection
    @CollectionTable(name = "board_comments", joinColumns = @JoinColumn(name = "board_id"))
    private List<Comment> comments = new ArrayList<>();
    
    @Column(name="write_date" ,length = 8)
	private String writeDate = LocalDate.now().toString().replace("-", "");
    
}
