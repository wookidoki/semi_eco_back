package com.kh.eco.like.model.service;

import com.kh.eco.like.model.vo.LikeResponse;

public interface LikeService {

	LikeResponse toggleLike(Long boardNo, int memberNo);
	
	LikeResponse commentLike(Long commentNo, int memberNo);
	
}
