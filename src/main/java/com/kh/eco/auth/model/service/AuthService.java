package com.kh.eco.auth.model.service;

import java.util.Map;

import com.kh.eco.member.model.dto.MemberLoginDTO;
import com.kh.eco.member.model.dto.MemberLogoutDTO;


public interface AuthService {
	
	Map<String, String> login(MemberLoginDTO member);
	
	void logout(MemberLogoutDTO member);

}