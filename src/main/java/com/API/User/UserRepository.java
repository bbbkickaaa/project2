package com.API.User;

import org.springframework.data.repository.Repository;

import com.API.User.Entity.User;

public interface UserRepository extends Repository<User,Long>{

	User save(User user);
    User findById(Long id);
    User findByUserid(String userid);
    void delete(User user);
}
