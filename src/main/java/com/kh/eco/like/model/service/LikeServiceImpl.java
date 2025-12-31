package com.kh.eco.like.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.comment.model.service.CommentService;
import com.kh.eco.like.model.dao.LikeMapper;
import com.kh.eco.like.model.vo.LikeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;
	private final CommentService commentService;
	
	@Override
	@Transactional
	public LikeResponse toggleLike(Long boardNo, int memberNo) {
		
		boolean isLiked;
		
		// 게시글 존재 여부
		int boardExists = likeMapper.existsBoard(boardNo);
		if(boardExists == 0) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다. boardNo = " + boardNo);
		}
		
		// 좋아요 중복 체크
		int result = likeMapper.existsBoardLike(boardNo, memberNo);
		
		
		// 좋아요 테이블에 boardNo의 행이 존재하는지 여부 -- 없으면 INSERT
		int likeCountRowExists = likeMapper.existsLikeCountRow(boardNo);
		if(likeCountRowExists == 0) {
			likeMapper.insertLikeCountRow(boardNo);
		}
		
		
		// 좋아요를 이미 누른 상태 -- 좋아요 취소 & 카운트 -1
		if(result > 0) {
			likeMapper.deleteBoardLike(boardNo, memberNo);
			likeMapper.decreaseBoardLikeCount(boardNo);
			isLiked = false;
		} else { // 좋아요를 안누른 상태 -- 좋아요 추가 & 카운트 +1
			likeMapper.insertBoardLike(boardNo, memberNo);
			likeMapper.increaseBoardLikeCount(boardNo);
			isLiked = true;
		}
		
		int likeCount = likeMapper.getBoardLikeCount(boardNo);
		
		return new LikeResponse(isLiked, likeCount);
		
	}
	
	@Override
	@Transactional
	public LikeResponse commentLike(Long commentNo, int memberNo) {
		
		boolean isLiked;
		
		// 좋아요 중복 여부
		int result = likeMapper.existsCommentLike(commentNo, memberNo);
		
		// 댓글_좋아요 테이블에 commentNo의 행이 존재하는지 여부
		int existsRow = likeMapper.existsCommentLikeRow(commentNo);
		if(existsRow < 1) {
			likeMapper.insertCommentLikeRow(commentNo);
		}
		
		// 좋아요를 이미 누른 상태 -- 좋아요 취소 & 카운트 -1
		if(result > 0) {
			likeMapper.deleteCommentLike(commentNo, memberNo);
			likeMapper.decreaseCommentLikeCount(commentNo);
			isLiked = false;
		} else { // 좋아요를 안누른 상태 -- 좋아요 추가 & 카운트 +1
			likeMapper.insertCommentLike(commentNo, memberNo);
			likeMapper.increaseCommentLikeCount(commentNo);
			isLiked = true;
		}
		
		int likeCount = likeMapper.getCommentLikeCount(commentNo);
		
		return new LikeResponse(isLiked, likeCount);
		
	}
	
}
