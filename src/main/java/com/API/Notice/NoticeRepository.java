package com.API.Notice;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.API.Notice.DTO.NoticeReviewDTO;
import com.API.Notice.Entity.Notice;

public interface NoticeRepository extends Repository<Notice, Long>{
	
	Notice save(Notice notice);
	Optional<Notice> findById(Long id);
	void deleteById(Long noticeId);
	
	@Query("SELECT new com.API.Notice.DTO.NoticeReviewDTO(b.id, b.title, u.id, u.nickname, b.views, b.likes, COUNT(bi)) " +
		       "FROM Notice b JOIN b.author u LEFT JOIN b.noticeImage bi " +
		       "GROUP BY b.id, b.title, u.id, u.nickname, b.views, b.likes " +
		       "ORDER BY b.id DESC") 
	Page<NoticeReviewDTO> findAllNoticeReviewDTOs(Pageable pageable);
	
}

