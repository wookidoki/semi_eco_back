package com.kh.eco.member.model.service;

import org.springframework.stereotype.Service;

import com.kh.eco.exception.MemberInfoDuplicatedException;
import com.kh.eco.member.model.dao.MemberMapper;
import com.kh.eco.member.model.dto.MemberSignUpDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberInfoDuplicateCheck {
	
	private final MemberMapper memberMapper;
	
	public void idDuplicateCheck(String memberId) {
		int count = memberMapper.countByMemberId(memberId);
		if(1 <= count) {
			throw new MemberInfoDuplicatedException("이미 존재하는 아이디입니다.");
		} 
	}
	
	public void phoneDuplicateCheck(String phone) {
		int count = memberMapper.countByPhone(phone);
		if(1 <= count) {
			throw new MemberInfoDuplicatedException("사용 불가능한 휴대전화 번호입니다.");
		} 
	}
	
	public void emailDuplicateCheck(String email) {
		int count = memberMapper.countByEmail(email);
		if(1 <= count) {
			throw new MemberInfoDuplicatedException("사용 불가능한 이메일입니다.");
		} 
	}
	
	
	

}