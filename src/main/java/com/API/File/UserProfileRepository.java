package com.API.File;

import org.springframework.data.repository.Repository;

import com.API.File.Entity.UserProfile;

public interface UserProfileRepository extends Repository<UserProfile, Long> {
	
	void save(UserProfile userProfile);

}
