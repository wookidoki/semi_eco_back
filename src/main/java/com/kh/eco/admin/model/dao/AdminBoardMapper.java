package com.kh.eco.admin.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.kh.eco.admin.model.dto.AdminBoardDTO;
import com.kh.eco.admin.model.dto.AttachmentDTO;

@Mapper
public interface AdminBoardMapper {
	
	// 게시글 전체조회
	List<AdminBoardDTO> findBoardAll(RowBounds rb);
	
	int countBoards(); 
	
	// 신고된 게시글 조회
	List<AdminBoardDTO> findReportedBoard(RowBounds rb);
	
	int countReportedBoards();
	
	// 게시글 상세 조회 > 게시글 테이블 및 작성자 조회
	AdminBoardDTO selectBoard(Long boardNo);
	// 게시글 상세 조회 > 첨부문서 조회
	List<AttachmentDTO> selectAttachemnt(Long boardNo);
	
	// 게시글 삭제(Status만 변경, 사실은 Update문)
	int deleteBoard(Long boardNo);
	
	// 게시글 복원
	int restoreBoard(Long boardNo);
	
	int selectReport(Long boardNo);
	
	int handleReport(Long boardNo);

}
