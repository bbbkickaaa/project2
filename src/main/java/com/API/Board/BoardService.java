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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.API.Alarm.AlarmRepository;
import com.API.Alarm.Entity.Alarm;
import com.API.Alarm.Entity.Alarm.AlarmType;
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
import com.API.File.FileService;
import com.API.Report.ReportRepository;
import com.API.Report.DTO.ReportDTO;
import com.API.User.UserRepository;
import com.API.User.DTO.UserDTO;
import com.API.User.Entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	FileService fileService;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	ReportRepository reportRepository;
	
	public ResponseEntity<Page<BoardReviewDTO>> findAll(String category3, String category2, String category1 ,String option, String content, Pageable pageable,Authentication authentication) {
		String userId = authentication.getName();
		Optional<User> userWrap = userRepository.findByUserid(userId);
		if(userWrap.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		User user = userWrap.get();
		Set<Integer> blockUsers = user.getBlockIds();
		
		
	    String queryString = "SELECT new com.API.Board.DTO.BoardReviewDTO(b.id, b.title, u.id, u.nickname, b.views, b.likes, COUNT(c), b.category) FROM Board b JOIN b.author u LEFT JOIN b.comments c";
	    String countQueryString = "SELECT COUNT(b) FROM Board b JOIN b.author u";

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
		    
		    if (!blockUsers.isEmpty()) {
		        if (isWhereAdded) {
		            whereClause += " AND ";
		        } else {
		            whereClause += " WHERE ";
		            isWhereAdded = true;
		        }
		        whereClause += "u.id NOT IN :blockUsers";
		    }

	    queryString += isWhereAdded ? whereClause : "";
	    queryString += " GROUP BY b.id, b.title, u.id, u.nickname, b.views, b.likes, b.category";
	    countQueryString += isWhereAdded ? whereClause : "";

	    
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
        if (!blockUsers.isEmpty()) {
        	countQuery.setParameter("blockUsers", blockUsers);
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
	    // 매개변수 바인딩
	    if (!blockUsers.isEmpty()) {
            query.setParameter("blockUsers", blockUsers);
        }
	    

	    query.setFirstResult((int) pageable.getOffset());
	    query.setMaxResults(pageable.getPageSize());

	    List<BoardReviewDTO> results = query.getResultList();

	    Page<BoardReviewDTO> page = new PageImpl<>(results, pageable, totalRows);
	    return ResponseEntity.ok(page);
	}
	
	@Transactional
	public ResponseEntity<String> postBoard(BoardPostDTO dto , Authentication authentication){
		Board board;
		try {
			if(dto.getBoardId() == null || dto.getBoardId().isEmpty()) {
				board = new Board();
			}else {
				Long boardId = Long.valueOf(dto.getBoardId());
				Optional<Board> boardWrap = boardRepository.findById(boardId);
				if(boardWrap.isEmpty()) {
					return ResponseEntity.status(HttpStatus.CONFLICT).build();
				}
			    board = boardWrap.get();
			}
			String idAsString = dto.getId();
			Long id = Long.valueOf(idAsString);
			User author = userRepository.findById(id).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + id));
			String title = dto.getTitle();
			String content = dto.getContent();
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
	
	@Transactional
	public ResponseEntity<String> alterBoard(BoardPostDTO dto , Authentication authentication){
		
		Board board;
		try {
			board = new Board();
			String BoardIdAsString = dto.getBoardId();
			Long boardId = Long.valueOf(BoardIdAsString);
			board.setId(boardId);
			String idAsString = dto.getId();
			Long id = Long.valueOf(idAsString);
			User author = userRepository.findById(id).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + id));
			String title = dto.getTitle();
			String content = dto.getContent();
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
		
	
	public ResponseEntity<?> getDetail(Long id, Authentication authentication){
		String userid = authentication.getName();
		Optional<Board> wrapBoard = boardRepository.findById(id);
		Optional<User> wrapUser = userRepository.findByUserid(userid);
		if(wrapBoard.isEmpty() || wrapUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		User user = wrapUser.get();
		Board board = wrapBoard.get();
		int boardIdInt = board.getId().intValue();
		BoardDTO boardDTO = BoardDTO.fromBoard(board);
		if(user.getLikeBoardId().contains(boardIdInt)) {
			boardDTO.setFavorite(true);
		}
	    List<CommentDTO> listDto = new ArrayList<>();
	    List<Comment> list = board.getComments();
	    list.stream().forEach(comment -> {
	    	CommentDTO dto = CommentDTO.fromCommentAndBoard(comment, board);
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
	public ResponseEntity<?> postRecommend(Map<String, Object> requestData) {
		int userId = Integer.parseInt(requestData.get("userIdx").toString());
		long boardId = Long.parseLong(requestData.get("BoardId").toString());

	    Optional<Board> board = boardRepository.findById(boardId);
	    return board.map(b -> {
	        Set<Integer> likesUsers = b.getLikesUsers();
	        if (likesUsers == null) {
	            likesUsers = new HashSet<>();
	            b.setLikesUsers(likesUsers);
	        }
	        if (likesUsers.contains(userId)) {
	            likesUsers.remove(userId);
	            b.setLikes(b.getLikes() - 1);
	            boardRepository.save(b);
	            return ResponseEntity.ok().body("추천 취소되었습니다.");
	        } else {
	            likesUsers.add(userId);
	            b.setLikes(b.getLikes() + 1);
	            boardRepository.save(b);
	            return ResponseEntity.ok().body("추천 되었습니다.");
	        }
	    }).orElseGet(() -> ResponseEntity.badRequest().build());
	}


	
	
	public ResponseEntity<?> postComment(Map<String, Object> requestData){
		try {
			String url = (String) requestData.get("whereParam");
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
			Alarm alarm = new Alarm();
			alarm.setContent("새로운 댓글이 달렸습니다.");
			alarm.setForwardNickname(author.getNickname());
			alarm.setForwarduserId(author.getId());
			alarm.setRecipientId(board.getAuthor().getId());
			alarm.setType(AlarmType.COMMENT);
			alarm.setReferencedId(boardid);
			alarm.setUrl(url);
			alarmRepository.save(alarm);
			
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

	public ResponseEntity<?> postFavorite(Map<String, Object> requestData) {
		long userId = Long.parseLong(requestData.get("userIdx").toString());
		long boardId = Long.parseLong(requestData.get("BoardId").toString());
		Integer boardInt = (int)boardId;
	    Optional<User> user = userRepository.findById(userId);
	    return user.map(u -> {
	        Set<Integer> likesUsers = u.getLikeBoardId();
	        if (likesUsers == null) {
	            likesUsers = new HashSet<>();
	            u.setLikeBoardId(likesUsers);
	        }
	        if (likesUsers.contains(boardInt)) {
	            likesUsers.remove(boardInt);
	            u.setLikeBoardId(likesUsers);
	            userRepository.save(u);
	            return ResponseEntity.ok().body("추천 취소되었습니다.");
	        } else {
	            likesUsers.add((int) boardId);
	            userRepository.save(u);
	            return ResponseEntity.ok().body("추천 되었습니다.");
	        }
	    }).orElseGet(() -> ResponseEntity.badRequest().build());
	}

	public ResponseEntity<Page<BoardReviewDTO>> favorite(Pageable pageable, Authentication authentication) {
		String userid = authentication.getName();
		Optional<User> wrapUser = userRepository.findByUserid(userid);
		if(wrapUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		User user = wrapUser.get();
		Set<Integer> list = user.getLikeBoardId();
		Page<BoardReviewDTO> dto = boardRepository.findAllBylist(pageable,list);
		return ResponseEntity.ok(dto);
	}

	public ResponseEntity<Page<ReportDTO>> report(Pageable pageable) {
		Page<ReportDTO> dto = reportRepository.findAllReports(pageable);
		return ResponseEntity.ok(dto);
	}

	

}

