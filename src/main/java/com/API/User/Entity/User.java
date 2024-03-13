package com.API.User.Entity;

import java.time.LocalDate; 
import java.util.Set;
import com.API.User.Etc.IdxToStringCoverter;
import com.API.User.Etc.RandomNicknameGenerator;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@SecondaryTables({
	@SecondaryTable(name="user_level" , pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id" ) , indexes = @Index(columnList = "id"))
	// @SecondaryTable(name="user_address", pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id")),
	// @SecondaryTable(name="user_info" , pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id")),
})
@Table(name = "users")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="user_id" , unique=true, nullable = false)
    private String userid;
	
	@Column(nullable = false)
    private String password;
    
	@Column(name="nick_name", nullable=false, length = 8)
    private String nickname = RandomNicknameGenerator.generateRandomNickname();
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="level", column=@Column(table="user_level", name="level")),
	    @AttributeOverride(name="points", column=@Column(table="user_level", name="points"))
	})
	private UserLevel userLevel;
	
	private String picture = "profile/default1.png";
	
	private String email;
	/*
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="address1", column = @Column(table="user_address", name="addr1")),
		@AttributeOverride(name="address2", column = @Column(table="user_address", name="addr2")),
		@AttributeOverride(name="zipcode", column = @Column(table="user_address"))
	})
	private UserAddress address;
	*/
	
	@Column(name="created_date" ,nullable = false,length = 8)
	private String createdDate = LocalDate.now().toString().replace("-", "");
	
	@Column(name="is_deleted" , nullable=false)
	private boolean isDeleted = false;
	
	@Column(name="deleted_date" ,length = 8)
	private String deletedDate;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;
	
	@Convert(converter = IdxToStringCoverter.class)
	@Column(name="block_id")
	private Set<Integer> blockIds;
	
	@Column(name="account_type", nullable = false)
	private String accountType;
	
	@Convert(converter = IdxToStringCoverter.class)
	@Column(name="like_board_id")
	private Set<Integer> likeBoardId;
	
	@Builder
	public User(String name, String email, String picture, UserRole role, String password, UserLevel userlevel , String nickname, String accountType) {
		this.setUserid(name);
		this.setEmail(email);
		this.setPicture(picture);
		this.setRole(role);
		this.setPassword(password);
		this.setUserLevel(userlevel);
		this.setNickname(nickname);
		this.setAccountType(accountType);
	}
	
	public User update(String name) {
		this.setUserid(name);
		return this;
	}
	
	public String getRoleKey() {
		return this.role.getKey();
	}

}
