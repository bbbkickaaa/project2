package com.API.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.API.Board.DTO.BoardAlterDTO;
import com.API.Board.DTO.BoardDTO;
import com.API.Board.DTO.BoardPostCountDTO;
import com.API.Board.DTO.BoardPostDTO;
import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.DTO.CommentDTO;
import com.API.Board.DTO.DeleteCommentDTO;
import com.API.Board.Entity.Board;
import com.API.Board.Entity.BoardCategory;
import com.API.Board.Entity.Comment;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	EntityManager entityManager;
	
	
	public ResponseEntity<Page<BoardReviewDTO>> findAll(String category3, String category2, String category1 ,String option, String content, Pageable pageable) {
	    String queryString = "SELECT new com.API.Board.DTO.BoardReviewDTO(b.id, b.title, u.id, u.nickname, b.views, b.likes, COUNT(c), b.category) FROM Board b JOIN b.author u LEFT JOIN b.comments c";
	    String countQueryString = "SELECT COUNT(b) FROM Board b JOIN b.author u LEFT JOIN b.comments c";

	    String whereClause = "";
	    boolean isWhereAdded = false;

		    if (category3 != null && !category3.isEmpty()) {
		        whereClause = " WHERE b.category.category1 = :category1 AND b.category.category2 = :category2 AND b.category.category3 = :category3 ";
		        isWhereAdded = true;
		    } else if (category2 != null && !category2.isEmpty()) {
		        whereClause = " WHERE b.category.category1 = :category1 AND b.category.category2 = :category2 ";
		        isWhereAdded = true;
		    } else if (category1 != null && !category1.isEmpty()) {
		        whereClause = " WHERE b.category.category1 = :category1 ";
		        isWhereAdded = true;
		    }

		    if (option != null && content != null && category1 != null) {
		        if (isWhereAdded) {
		            whereClause += " AND ";
		        }
		        if ("title".equals(option)) {
		            whereClause += "b.title LIKE :content";
		        } else if ("index".equals(option)) {
		            whereClause += "b.id = :indexParam";
		        } else if ("author".equals(option)) {
		            whereClause += "u.nickname LIKE :content";
		        }
		        isWhereAdded = true;
		    }else if (option != null && content != null && category1 == null) {
		        if (!isWhereAdded) {
		            whereClause += " WHERE ";
		        }
		        if ("title".equals(option)) {
		            whereClause += "b.title LIKE :content";
		        } else if ("index".equals(option)) {
		            whereClause += "b.id = :indexParam";
		        } else if ("author".equals(option)) {
		            whereClause += "u.nickname LIKE :content";
		        }
		        isWhereAdded = true;
		    }

	    queryString += isWhereAdded ? whereClause : "";
	    countQueryString += isWhereAdded ? whereClause : "";

	    queryString += " GROUP BY b.id, b.title, u.id, u.nickname, b.views, b.likes, b.category";
	    
	    TypedQuery<Long> countQuery = entityManager.createQuery(countQueryString, Long.class);

	    // 파라미터 설정
	    if (category3 != null && !category3.isEmpty()) {
	        countQuery.setParameter("category3", category3);
	        countQuery.setParameter("category2", category2);
	        countQuery.setParameter("category1", category1);
	    } else if (category2 != null && !category2.isEmpty()) {
	        countQuery.setParameter("category2", category2);
	        countQuery.setParameter("category1", category1);
	    } else if (category1 != null && !category1.isEmpty()) {
	        countQuery.setParameter("category1", category1);
	    }

	    if (option != null && content != null) {
	        if ("index".equals(option)) {
	            countQuery.setParameter("indexParam", content);
	        } else {
	            countQuery.setParameter("content", "%" + content + "%");
	        }
	    }
	    queryString += " ORDER BY b.id DESC";
	    long totalRows = countQuery.getSingleResult();

	    // 쿼리 실행
	    TypedQuery<BoardReviewDTO> query = entityManager.createQuery(queryString, BoardReviewDTO.class);

	 // BoardReviewDTO 쿼리 파라미터 설정 (Count 쿼리와 동일)
	    if (category3 != null && !category3.isEmpty()) {
	        query.setParameter("category3", category3);
	        query.setParameter("category2", category2);
	        query.setParameter("category1", category1);
	    } else if (category2 != null && !category2.isEmpty()) {
	        query.setParameter("category2", category2);
	        query.setParameter("category1", category1);
	    } else if (category1 != null && !category1.isEmpty()) {
	        query.setParameter("category1", category1);
	    }
	    
	    if (option != null && content != null ) {
	        if ("index".equals(option)) {
	        	query.setParameter("indexParam", content);
	        } else {
	        	query.setParameter("content", "%" + content + "%");
	        }
	    }
	    

	    query.setFirstResult((int) pageable.getOffset());
	    query.setMaxResults(pageable.getPageSize());

	    List<BoardReviewDTO> results = query.getResultList();

	    Page<BoardReviewDTO> page = new PageImpl<>(results, pageable, totalRows);
	    return ResponseEntity.ok(page);
	}
	
	public ResponseEntity<String> postBoard(BoardPostDTO dto){
		
		Board board = new Board();
		try {
			String idAsString = dto.getId();
			Long id = Long.valueOf(idAsString);
			User author = userRepository.findById(id).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + id));
			String title = dto.getTitle();
			String content = dto.getContent();
			if (dto.getBoardId() == null) {
				BoardCategory category = new BoardCategory();
				category.setCategory1(dto.getCategory1());
				category.setCategory2(dto.getCategory2());
				category.setCategory3(dto.getCategory3());
				board.setCategory(category);
				board.setTitle(title);
				board.setContent(content);
				board.setAuthor(author);
				boardRepository.save(board);

				
				return ResponseEntity.status(HttpStatus.OK).body("정상 등록 되었습니다.");
				
				}else {
					
					Long boardId = Long.valueOf(dto.getBoardId());
					Optional<Board> board2o = boardRepository.findById(boardId);
					Board board2 = board2o.get();
					board2.setAlterDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
					board2.setContent(content);
					board2.setTitle(title);
					boardRepository.save(board2);
					return ResponseEntity.status(HttpStatus.OK).body("정상 수 되었습니다.");
				}
				
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
		
	
	public ResponseEntity<?> getDetail(Long id){
		Optional<Board> wrapBoard = boardRepository.findById(id);
		if(wrapBoard.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		Board board = wrapBoard.get();
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(board.getId());
	    boardDTO.setTitle(board.getTitle());
	    boardDTO.setContent(board.getContent());
	    boardDTO.setUserIdx(board.getAuthor().getId());
	    boardDTO.setLikes(board.getLikes());
	    boardDTO.setViews(board.getViews());
	    boardDTO.setCategory(board.getCategory());
	    boardDTO.setWriteDate(board.getWriteDate());
	    boardDTO.setAlterDate(board.getAlterDate());
	    boardDTO.setPicture(board.getAuthor().getPicture());
	    boardDTO.setLevel(board.getAuthor().getUserLevel().getLevel());
	    List<CommentDTO> listDto = new ArrayList<>();
	    List<Comment> list = board.getComments();
	    list.stream().forEach(comment -> {
	    	CommentDTO dto = new CommentDTO();
	    	dto.setContent(comment.getContent());
	    	dto.setNickname(comment.getAuthor().getNickname());
	    	dto.setUserid(comment.getAuthor().getId());
	    	dto.setWriteDate(comment.getWriteDate());
	    	dto.setCommentId(comment.getId());
	    	dto.setPicture(comment.getAuthor().getPicture());
	    	dto.setLevel(board.getAuthor().getUserLevel().getLevel());
	    	listDto.add(dto);
	    });
	    boardDTO.setComments(listDto);
	    boardDTO.setNickname(board.getAuthor().getNickname());
		return ResponseEntity.ok().body(boardDTO);
		
	}
	
	public ResponseEntity<?> findbyIdOnlyAlter(Long id){
		
		Optional<Board> board = boardRepository.findById(id);
		if(board.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		else {
			BoardAlterDTO dto = new BoardAlterDTO();
			Board boards = board.get();
			dto.setTitle(boards.getTitle());
			dto.setContent(boards.getContent());
			dto.setId(id);
			dto.setUserIdx(boards.getAuthor().getId());
			
			return ResponseEntity.ok(dto);
		}
	}
	
	public ResponseEntity<?> countUserPostsAndComments(Long id){
		BoardPostCountDTO dto = new BoardPostCountDTO();
		Long postCount = boardRepository.countPostsByAuthorId(id);
		List<Comment> CommentCount = commentRepository.findByauthorId(id);
		Long count = CommentCount.stream().count();
		dto.setCommentCount(count);
		dto.setPostCount(postCount);
		return ResponseEntity.ok(dto);
	}
	
	public ResponseEntity<?> postViews(String boardIdS){
		Long boardId = Long.valueOf(boardIdS);
		Optional<Board> board = boardRepository.findById(boardId);
		if(board.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		board.get().setViews(board.get().getViews() + 1);
        boardRepository.save(board.get());
        
        return ResponseEntity.ok().body("조회수 증가되었습니다.");
	}
	
	public ResponseEntity<?> deleteBoard(String boardIdS){
		Long boardId = Long.valueOf(boardIdS);
		boardRepository.deleteById(boardId);
		
		return ResponseEntity.ok().body("삭제 되었습니다.");
	}
	public ResponseEntity<?> postRecommend(Map<String, Object> requestData){
		int userIdAsString = (Integer) requestData.get("userIdx");
		int userId = Integer.valueOf(userIdAsString);
		int boardidAsString = (Integer) requestData.get("id");
		Long boardLong = Long.valueOf(boardidAsString);
		
		Optional<Board> board = boardRepository.findById(boardLong);
		if(board.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		if (board.get().getLikesUsers() == null || !board.get().getLikesUsers().stream().anyMatch(user -> user.equals(userId))) {
		    board.get().setLikes(board.get().getLikes() + 1);
		    Set<Integer> set = new HashSet<>();
		    set.add(userId);
		    board.get().setLikesUsers(set);
		    boardRepository.save(board.get());
		    return ResponseEntity.ok().body("추천되었습니다.");
		} else {
		    return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 추천되었습니다.");
		}
	}
	
	
	public ResponseEntity<?> postComment(Map<String, Object> requestData){
		try {
			String userIdAsString = (String) requestData.get("userIdx");
			Long userid = Long.valueOf(userIdAsString);
			int boardidAsInt = (Integer) requestData.get("BoardId");
			Long boardid = Long.valueOf(boardidAsInt);
			String content = (String) requestData.get("comment");
			Optional<Board> boards = boardRepository.findById(boardid);
			Board board = boards.get();
			User author = userRepository.findById(userid).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + userid));
			Comment comment = new Comment();
			comment.setAuthor(author);
			comment.setContent(content);
			
			if (board.getComments() == null) {
			    board.setComments(new ArrayList<>());
			}
			List<Comment> comments = board.getComments();
			comments.add(comment);
			board.setComments(comments);
			boardRepository.save(board);
			
			Board board2 = boardRepository.findById(boardid).get();
			List<CommentDTO> comments2 = new ArrayList<>();
			board2.getComments().stream().forEach(list -> {
				CommentDTO dto = new CommentDTO();
				dto.setCommentId(list.getId());
				dto.setNickname(list.getAuthor().getNickname());
				dto.setContent(list.getContent());
				dto.setUserid(list.getAuthor().getId());
				dto.setWriteDate(list.getWriteDate());
				comments2.add(dto);
			}
);
			return ResponseEntity.status(HttpStatus.OK).body(comments2);
			}
				catch (ClassCastException e) {
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
	
	public ResponseEntity<?> deleteComment(DeleteCommentDTO dto){
		Long BoardId = dto.getBoardId();
		Long CommentId = dto.getCommentId();
		Optional<Board> boardOptional = boardRepository.findById(BoardId);
		if (boardOptional.isPresent()) {
	        Board board = boardOptional.get();
	        boolean removed = board.getComments().removeIf(comment -> comment.getId().equals(CommentId));
		if (removed) {
        boardRepository.save(board);
        return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");
		}}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 게시판이 없습니다.");
		}
	    
	}

	
}

