package com.kh.eco.comment.model.service;

import java.util.List;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.comment.model.dto.CommentDTO;
import com.kh.eco.comment.model.dto.CommentReportDTO;
import com.kh.eco.comment.model.vo.CommentVO;

public interface CommentService {
	
	/**
	 * 댓글 작성
	 * @param comment
	 * @param userDetails
	 * @return
	 */
	CommentVO insertComment(CommentDTO comment, CustomUserDetails userDetails);
	
	/**
	 * 댓글 조회
	 * @param boardNo
	 * @return
	 */
	List<CommentDTO> findAll(Long boardNo);
	
	/**
	 * 댓글 존재 여부 확인
	 * @param CommentNo
	 * @return
	 */
	int existById(Long CommentNo);
	
	/**
	 * 댓글 신고하기
	 * @param reportDTO
	 * @return
	 */
	int commentReport(CommentReportDTO reportDTO);
	
	/**
	 * 댓글 본인 검증
	 * @param commentNo
	 * @param mno
	 * @return
	 */
	boolean isOwner(Long commentNo, int mno);
	
	/**
	 * 댓글 삭제하기
	 * @param commentNo
	 * @return
	 */
	int deleteComment(Long commentNo);
	
	/**
	 * 댓글 수정하기
	 * @param comment
	 * @return
	 */
	int updateComment(CommentDTO comment);
	
}

