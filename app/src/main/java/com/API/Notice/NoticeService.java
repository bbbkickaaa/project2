package com.API.Notice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.API.Notice.DTO.NoticeDTO;
import com.API.Notice.DTO.NoticePostDTO;
import com.API.Notice.DTO.NoticeReviewDTO;
import com.API.Notice.Entity.Notice;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoticeService {
	
	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	UserRepository userRepository;

	public ResponseEntity<?> postNotice(NoticePostDTO dto, Authentication authentication) {

		Notice notice;
		try {
			if(dto.getNoticeId() == null || dto.getNoticeId() == 0) {
				notice = new Notice();
			}else {
				Long boardId = Long.valueOf(dto.getNoticeId());
				Optional<Notice> boardWrap = noticeRepository.findById(boardId);
				if(boardWrap.isEmpty()) {
					return ResponseEntity.status(HttpStatus.CONFLICT).build();
				}
				notice = boardWrap.get();
			}
			Long id  = dto.getUserIdx();
			User author = userRepository.findById(id).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + id));
			String title = dto.getTitle();
			String content = dto.getContent();
			notice.setTitle(title);
			notice.setContent(content);
			notice.setAuthor(author);
			noticeRepository.save(notice);
			return ResponseEntity.status(HttpStatus.OK).body("정상 등록 되었습니다.");
				
			}catch (ClassCastException e) {
					    log.error("ClassCastException occurred", e);
			    return ResponseEntity.badRequest().build(); 
			} catch (NullPointerException e) {
			    log.error("NullPointerException occurred", e);
			    return ResponseEntity.status(HttpStatus.CONFLICT).build();
			} catch(EntityNotFoundException e) {
			    log.error("EntityNotFoundException occurred", e);
			    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
	}

	public ResponseEntity<?> alterNotice(NoticePostDTO dto, Authentication authentication) {
		Notice notice;
		try {
			Long NoticeId = dto.getNoticeId();
			Optional<Notice> boardWrap = noticeRepository.findById(NoticeId);
			if(boardWrap.isEmpty()) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			notice = boardWrap.get();
			Long id = dto.getUserIdx();
			User author = userRepository.findById(id).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + id));
			String title = dto.getTitle();
			String content = dto.getContent();
			notice.setTitle(title);
			notice.setContent(content);
			notice.setAuthor(author);
			noticeRepository.save(notice);
			return ResponseEntity.status(HttpStatus.OK).body("정상 등록 되었습니다.");
				
			}catch (ClassCastException e) {
					    log.error("ClassCastException occurred", e);
			    return ResponseEntity.badRequest().build(); 
			} catch (NullPointerException e) {
			    log.error("NullPointerException occurred", e);
			    return ResponseEntity.status(HttpStatus.CONFLICT).build();
			} catch(EntityNotFoundException e) {
			    log.error("EntityNotFoundException occurred", e);
			    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
	}

	public ResponseEntity<?> getDetail(Long id, Authentication authentication) {
		String userid = authentication.getName();
		Optional<Notice> wrapBoard = noticeRepository.findById(id);
		Optional<User> wrapUser = userRepository.findByUserid(userid);
		if(wrapBoard.isEmpty() || wrapUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		User user = wrapUser.get();
		Notice notice = wrapBoard.get();
		int boardIdInt = notice.getId().intValue();
		NoticeDTO noticeDTO = NoticeDTO.fromNotice(notice);
		if(user.getLikeBoardId().contains(boardIdInt)) {
			noticeDTO.setFavorite(true);
		}
		noticeDTO.setNickname(notice.getAuthor().getNickname());
		return ResponseEntity.ok().body(noticeDTO);
		
	}

	public ResponseEntity<?> findbyIdOnlyAlter(Long id) {
		Optional<Notice> notice = noticeRepository.findById(id);
		if(notice.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		else {
			NoticePostDTO dto = new NoticePostDTO();
			Notice notices = notice.get();
			dto.setTitle(notices.getTitle());
			dto.setContent(notices.getContent());
			dto.setUserIdx(notices.getAuthor().getId() );
			dto.setNoticeId(id);
			return ResponseEntity.ok(dto);
		}
	}

	public ResponseEntity<?> postViews(String noticeIds) {
		Long noticeId = Long.valueOf(noticeIds);
		Optional<Notice> notice = noticeRepository.findById(noticeId);
		if(notice.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		notice.get().setViews(notice.get().getViews() + 1);
        noticeRepository.save(notice.get());
        
        return ResponseEntity.ok().body("조회수 증가되었습니다.");
	}

	public ResponseEntity<?> deleteNotice(String noticeIds) {
		Long noticeId = Long.valueOf(noticeIds);
		noticeRepository.deleteById(noticeId);
		
		return ResponseEntity.ok().body("삭제 되었습니다.");
	}

	public ResponseEntity<?> postRecommend(Map<String, Object> requestData) {
		int userId = Integer.parseInt(requestData.get("userIdx").toString());
		long boardId = Long.parseLong(requestData.get("noticeId").toString());

	    Optional<Notice> noticeWrap = noticeRepository.findById(boardId);
	    return noticeWrap.map(b -> {
	        Set<Integer> likesUsers = b.getLikesUsers();
	        if (likesUsers == null) {
	            likesUsers = new HashSet<>();
	            b.setLikesUsers(likesUsers);
	        }
	        if (likesUsers.contains(userId)) {
	            likesUsers.remove(userId);
	            b.setLikes(b.getLikes() - 1);
	            noticeRepository.save(b);
	            return ResponseEntity.ok().body("추천 취소되었습니다.");
	        } else {
	            likesUsers.add(userId);
	            b.setLikes(b.getLikes() + 1);
	            noticeRepository.save(b);
	            return ResponseEntity.ok().body("추천 되었습니다.");
	        }
	    }).orElseGet(() -> ResponseEntity.badRequest().build());
	}

	public ResponseEntity<Page<NoticeReviewDTO>> findAll(Pageable pageable, Authentication authentication) {
		Page<NoticeReviewDTO> dto = noticeRepository.findAllNoticeReviewDTOs(pageable);
		return ResponseEntity.ok(dto);
	}
	

}
