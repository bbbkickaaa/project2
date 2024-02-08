package com.API.User;

import java.util.Optional; 

import org.springframework.data.repository.Repository;
import com.API.User.Entity.User;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User,Long>{

	User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUserid(String userId);
    void delete(User user);
    Optional<User> findByEmail(String email);
}
