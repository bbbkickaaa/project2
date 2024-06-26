package com.API.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.API.Board.DTO.BoardReviewDTO;
import com.API.User.Entity.User;

@org.springframework.stereotype.Repository
public interface UserRepository extends Repository<User,Long>{

	User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUserid(String userId);
    void delete(User user);
    Optional<Set<User>> findByEmail(String email);
    Page<User> findByIdIn(Set<Long> userFriendIds, Pageable pageable);
   
}
