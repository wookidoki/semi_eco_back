package com.kh.eco.like.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeMapper {
	// 게시글 존재 여부
	int existsBoard(Long boardNo);
	
	// 좋아요 개수 행 존재 여부
	int existsLikeCountRow(Long boardNo);
	
	// 좋아요 행 새로 추가하기
	int insertLikeCountRow(Long boardNo);
	
	// 좋아요 여부
	int existsBoardLike(@Param("boardNo")Long boardNo, @Param("memberNo")int memberNo);
	
	// 좋아요 추가
	int insertBoardLike(@Param("boardNo")Long boardNo, @Param("memberNo")int memberNo);
	
	// 좋아요 증가
	int increaseBoardLikeCount(Long boardNo);
	
	// 좋아요 취소
	int deleteBoardLike(@Param("boardNo")Long boardNo, @Param("memberNo")int memberNo);
	
	// 좋아요 감소
	int decreaseBoardLikeCount(Long boardNo);
	
	// 좋아요 개수
	int getBoardLikeCount(Long boardNo);
	
	/**
	 * 댓글 좋아요 중복 여부 
	 */
	int existsCommentLike(@Param("commentNo") Long commentNo, @Param("memberNo") int memberNo);
	
	/**
	 * 댓글 좋아요 행 존재 여부
	 */
	int existsCommentLikeRow(Long commentNo);
	
	/**
	 * 댓글 좋아요 행 INSERT
	 */
	int insertCommentLikeRow(Long commentNo);
	
	/**
	 * 댓글 좋아요 추가
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int insertCommentLike(@Param("commentNo")Long commentNo, @Param("memberNo")int memberNo);
		
	/**
	 * 댓글 좋아요 증가
	 * @param boardNo
	 * @return
	 */
	int increaseCommentLikeCount(Long commentNo);
		
	/**
	 * 댓글 좋아요 취소
	 * @param boardNo
	 * @param memberNo
	 * @return
	 */
	int deleteCommentLike(@Param("commentNo")Long commentNo, @Param("memberNo")int memberNo);
		
	/**
	 * 댓글 좋아요 감소
	 * @param boardNo
	 * @return
	 */
	int decreaseCommentLikeCount(Long commentNo);
		
	/**
	 * 댓글 좋아요 개수
	 * @param boardNo
	 * @return
	 */
	int getCommentLikeCount(Long commentNo);
	
	
	
	
	
	
	
	
	
	
	/* 
	 int result = likeMapper.existsBoardLike(boardNo, memberNo);
		
		int boardExists = likeMapper.existsBoard(boardNo);
		if(boardExists == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다. boardNo = " + boardNo);
		}
		
		int likeCountRowExists = likeMapper.existsLikeCountRow(boardNo);
		if(likeCountRowExists == 0) {
			likeMapper.insertLikeCountRow(boardNo)
		}
	 * */
}
