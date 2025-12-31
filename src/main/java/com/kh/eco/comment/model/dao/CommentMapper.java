package com.kh.eco.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.eco.comment.model.dto.CommentDTO;
import com.kh.eco.comment.model.dto.CommentReportDTO;
import com.kh.eco.comment.model.vo.CommentVO;

@Mapper
public interface CommentMapper {
	/**
	 * 댓글 작성
	 * DTO -> VO 로 가공된 값(댓글)
	 * @param c
	 */
	void insertComment(CommentVO c);
	
	/**
	 * 댓글 조회
	 */
	List<CommentDTO> findAll(Long boardNo);
	
	/**
	 * 댓글 존재여부 조회
	 */
	int existById(Long commentNo);
	
	/**
	 * 댓글 신고
	 */
	int commentReport(CommentReportDTO reportDTO);
	
	/**
	 * 댓글 본인여부 조회
	 */
	boolean isOwner(@Param("commentNo")Long commentNo, @Param("memberNo")Long memberNo);
	
	/**
	 * 댓글 삭제
	 */
	int deleteComment(Long commentNo);
	
	/**
	 * 댓글 수정
	 */
	int updateComment(CommentDTO comment);

}