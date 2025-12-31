package com.kh.eco.admin.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.eco.admin.model.dto.AdminCommentDTO;

@Mapper
public interface AdminCommentMapper {
	
	// 댓글 전체조회
	List<AdminCommentDTO> findCommentAll(RowBounds rb);
	
	int countComments();
	
	// 신고된 댓글 조회
	List<AdminCommentDTO> findReportedComment(RowBounds rb);
	
	int countReportedComments();
	
	// 댓글 상세 조회
	AdminCommentDTO selectComment(Long commentNo);
	
	// 게시글 삭제(Status만 변경, 사실은 Update문)
	int deleteComment(Long commentNo);
	
	// 게시글 복원
	int restoreComment(Long commentNo);
	
	int selectReport(Long commentNo);
	
	int handleReport(Long commentNo);
	
}
