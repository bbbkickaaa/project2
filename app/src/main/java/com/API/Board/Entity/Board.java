package com.API.Board.Entity;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.API.User.Entity.User;
import com.API.User.Etc.IdxToStringCoverter;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.SecondaryTables;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "board")
@SecondaryTable(name="board_category" , pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id" ) , indexes = @Index(columnList = "id"))
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @Column(nullable = false)
    private int likes = 0;
    
    @Convert(converter = IdxToStringCoverter.class)
    @Column(name = "likes_users")
    private Set<Integer> likesUsers;

    @Column(nullable = false)
    private int views = 0;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    
    @Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="category1", column=@Column(table="board_category", name="category1")),
	    @AttributeOverride(name="category2", column=@Column(table="board_category", name="category2")),
	    @AttributeOverride(name="category3", column=@Column(table="board_category", name="category3"))
	})
    private BoardCategory category; 
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoardImage> boardImage;
    
    @Column(name = "write_date", length = 14)
    private String writeDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    
    @Column(name="alter_date" ,length = 14)
    private String alterDate;
    
}
