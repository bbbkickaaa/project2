package com.API.User.Entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SecondaryTables({
	@SecondaryTable(name="user_address", pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id")),
	@SecondaryTable(name="user_info" , pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id")),
	@SecondaryTable(name="user_level" , pkJoinColumns = @PrimaryKeyJoinColumn(name="id", referencedColumnName = "id"))
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
    
	@Column(name="nick_name", nullable=false)
    private String nickname;
	
	@Embedded
	private UserInfo info;
	
	@Embedded
	private UserLevel userLevel;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="address1", column = @Column(table="user_address", name="addr1")),
		@AttributeOverride(name="address2", column = @Column(table="user_address", name="addr2")),
		@AttributeOverride(name="zipcode", column = @Column(table="user_address"))
	})
	private UserAddress address;
	
	@Column(name="created_date" ,nullable = false,length = 8)
	private String createdDate;
	
	@Column(name="deleted_date" ,length = 8)
	private String deletedDate;
	
	@ElementCollection
    private Set<UserRole> roles = new HashSet<>();
}
