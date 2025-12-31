package com.kh.eco.comment.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.service.BoardService;
import com.kh.eco.board.model.service.FeedService;
import com.kh.eco.comment.model.dao.CommentMapper;
import com.kh.eco.comment.model.dto.CommentDTO;
import com.kh.eco.comment.model.dto.CommentReportDTO;
import com.kh.eco.comment.model.vo.CommentVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final BoardService boardService;
	private final FeedService feedService;
	private final CommentMapper commentMapper;
	
	/**
	 * 댓글 작성
	 */
	@Override
	public CommentVO insertComment(CommentDTO comment, CustomUserDetails userDetails) { 
		
		feedService.selectFeedList(null, null, comment.getRefBno());
		String memberId = userDetails.getUsername();
		
		CommentVO c = CommentVO.builder()
				               .refMno(comment.getRefMno())
				               .refBno(comment.getRefBno())
				               .commentContent(comment.getCommentContent())
				               .build();
		
		commentMapper.insertComment(c);
		return c;
		
	}
	
	/**
	 * 댓글 조회
	 */
	@Override
	public List<CommentDTO> findAll(Long boardNo) {
		
		feedService.selectFeedList(null, null, boardNo);
		
		List<CommentDTO> resultSet =  commentMapper.findAll(boardNo);
		log.info("결과들: ", resultSet);
		return resultSet;
	}
	
	/**
	 * 댓글 존재 여부
	 */
	@Override
	public int existById(Long commentNo) {
		int result = commentMapper.existById(commentNo);
		if(result < 1) {
			throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
		} else {
			return result;
		}
	}
	
	/**
	 * 댓글 신고
	 */
	@Override
	public int commentReport(CommentReportDTO reportDTO) {
			
		int result = commentMapper.commentReport(reportDTO);
		if (result < 1) {
			throw new IllegalArgumentException("신고접수가 정상적으로 이루어지지 않았습니다.");
		} else {
			return result;
		}
	}
	
	/**
	 * 댓글 본인 여부
	 */
	@Override
	public boolean isOwner(Long commentNo, int mno) {
		// 매퍼 파라미터 타입 불일치 이슈로 REF_MNO 형변환 (Integer -> Long)

		Long memberNo = (long)mno;
		boolean result = commentMapper.isOwner(commentNo, memberNo);
		if(!result) {
			throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
		} else {
			return result;
		}
		
	}
	
	/**
	 * 댓글 삭제
	 */
	@Override
	public int deleteComment(Long commentNo) {
		
		int result = commentMapper.deleteComment(commentNo);
		if(result < 1) {
			throw new IllegalArgumentException("댓글 삭제에 실패하였습니다.");
		} else {
			return result;
		}
	}
	
	/**
	 * 댓글 수정
	 */
	@Override
	public int updateComment(CommentDTO comment) {
		
		int result = commentMapper.updateComment(comment);
		if(result < 1) {
			throw new IllegalArgumentException("댓글 수정에 실패하였습니다.");
		} else {
			return result;
		}
	}
	
	
	
	
	
}

