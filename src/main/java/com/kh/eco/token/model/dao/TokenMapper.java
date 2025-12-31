package com.kh.eco.token.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.eco.member.model.dto.MemberLogoutDTO;
import com.kh.eco.token.model.vo.RefreshToken;

@Mapper
public interface TokenMapper {
	
	int saveToken(RefreshToken token);
	
	void deleteToken(String memberId);

	RefreshToken findByToken(String refreshToken);
	
	int deleteTokenForLogout(MemberLogoutDTO member);
	
}