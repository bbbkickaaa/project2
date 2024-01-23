package com.API.User.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserAddress {
	
	@Column(name="addr1",nullable = true)
	private String address1;
	
	@Column(name="addr2",nullable = true)
	private String address2;
	
	@Column(name="zipcod",nullable = true)
	private String zipcode;
	

}
