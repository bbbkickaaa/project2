package com.API.User.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

//Not Work Now

@Embeddable
@Getter
@NoArgsConstructor
public class UserAddress {
	
	@Column(name="addr1")
	private String address1;
	
	@Column(name="addr2")
	private String address2;
	
	@Column(name="zipcod")
	private String zipcode;
	

}
