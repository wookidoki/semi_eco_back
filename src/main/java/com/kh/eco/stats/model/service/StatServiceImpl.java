package com.kh.eco.stats.model.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.stats.model.dao.StatMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatServiceImpl implements StatService {

    private final StatMapper statMapper;

    @Override
    public Map<String, Object> getLandingStats() {
        return statMapper.selectLandingStats();
    }

	@Override
	public Map<String, Object> getDashboardStats() {
		return statMapper.selectDashboardStats();
	}

	@Override
	public Map<String, Object> getMainpage() {

		return statMapper.selectMainpage();
	}

	@Override
	public List<Map<String, Object>> getRankingStats() {
		
		return statMapper.getRankingStats();
	}

	/**
	 * 오늘의 참여자 수
	 */
	@Override
	public int todayParticipants(String category) {
		return statMapper.todayParticipants(category);
	}
	
	/**
	 * 오늘의 새 글
	 */
	@Override
	public int todayPost(String category) {
		return statMapper.todayPost(category);
	}
}