package com.kh.eco.stats.model.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatMapper {
    Map<String, Object> selectLandingStats();

	Map<String, Object> selectDashboardStats();

	Map<String, Object> selectMainpage();

	List<Map<String, Object>> getRankingStats();

	int todayParticipants(String category);
	
	int todayPost(String category);
	
}