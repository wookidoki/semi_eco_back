package com.kh.eco.stats.model.service;
import java.util.List;
import java.util.Map;

public interface StatService {
    Map<String, Object> getLandingStats();

	Map<String, Object> getDashboardStats();

	Map<String, Object> getMainpage();

	List<Map<String, Object>> getRankingStats();

	/**
	    * 특정 카테고리의 오늘의 참여자 수를 조회합니다.
	    *
	    * @param category 카테고리 코드
	    * @return 오늘의 참여자 수
	    */
		int todayParticipants(String category);
		
		
		/**
	     * 특정 카테고리의 오늘의 게시글 작성 수를 조회합니다.
	     *
	     * @param category 카테고리 코드
	     * @return 오늘의 게시글 수
	     */
		int todayPost(String category);
}