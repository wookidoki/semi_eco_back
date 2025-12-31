package com.kh.eco.admin.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.admin.model.dao.AdminMemberMapper;
import com.kh.eco.admin.model.dto.AdminMemberDTO;
import com.kh.eco.common.dto.AdminUpdateRequest;
import com.kh.eco.exception.DuplicateException;
import com.kh.eco.exception.InvalidRequestException;
import com.kh.eco.exception.ResourceNotFoundException;
import com.kh.eco.member.model.service.MemberInfoDuplicateCheck;
import com.kh.eco.token.model.dao.TokenMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 회원 관리 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

	private final AdminMemberMapper adminMemberMapper;
	private final MemberInfoDuplicateCheck duplicateCheck;
	private final PasswordEncoder passwordEncoder;
	private final TokenMapper tokenMapper;

	@Override
	public AdminMemberDTO findMemberById(String memberId) {
		AdminMemberDTO member = adminMemberMapper.findMemberById(memberId);
		if (member == null) {
			throw ResourceNotFoundException.member(memberId);
		}
		return member;
	}

	@Override
	@Transactional
	public void updateMemberField(AdminUpdateRequest request) {
		String field = request.getField().toLowerCase();

		switch (field) {
			case "id" -> updateMemberId(request);
			case "password" -> updatePassword(request);
			case "phone" -> updatePhone(request);
			case "email" -> updateEmail(request);
			case "region" -> updateRegion(request);
			case "point" -> updatePoint(request);
			case "status" -> updateStatus(request);
			case "role" -> updateRole(request);
			case "name" -> updateName(request);
			default -> throw new InvalidRequestException("지원하지 않는 필드입니다: " + field);
		}
	}

	private void updateMemberId(AdminUpdateRequest request) {
		duplicateCheck.idDuplicateCheck(request.getNewValue());

		int result = adminMemberMapper.updateMemberIdByAdmin(
				request.getMemberNo(), request.getNewValue());
		if (result == 0) {
			throw new InvalidRequestException("아이디 변경 실패");
		}
	}

	private void updatePassword(AdminUpdateRequest request) {
		String encodedPassword = passwordEncoder.encode(request.getNewValue());

		int result = adminMemberMapper.updatePasswordByAdmin(
				request.getMemberNo(), encodedPassword);
		if (result == 0) {
			throw new InvalidRequestException("비밀번호 변경 실패");
		}
	}

	private void updatePhone(AdminUpdateRequest request) {
		duplicateCheck.phoneDuplicateCheck(request.getNewValue());

		int result = adminMemberMapper.updatePhoneByAdmin(
				request.getMemberNo(), request.getNewValue());
		if (result == 0) {
			throw new InvalidRequestException("전화번호 변경 실패");
		}
	}

	private void updateEmail(AdminUpdateRequest request) {
		duplicateCheck.emailDuplicateCheck(request.getNewValue());

		int result = adminMemberMapper.updateEmailByAdmin(
				request.getMemberNo(), request.getNewValue());
		if (result == 0) {
			throw new InvalidRequestException("이메일 변경 실패");
		}
	}

	private void updateRegion(AdminUpdateRequest request) {
		Integer regionNo = adminMemberMapper.findRegionNoByName(request.getNewValue());
		if (regionNo == null) {
			throw new InvalidRequestException("등록된 지역명이 아닙니다: " + request.getNewValue());
		}

		int result = adminMemberMapper.updateRegionByAdmin(request.getMemberNo(), regionNo);
		if (result == 0) {
			throw new InvalidRequestException("지역 변경 실패");
		}
	}

	private void updatePoint(AdminUpdateRequest request) {
		try {
			Long point = Long.parseLong(request.getNewValue());
			int result = adminMemberMapper.updatePointByAdmin(request.getMemberNo(), point);
			if (result == 0) {
				throw new InvalidRequestException("포인트 변경 실패");
			}
		} catch (NumberFormatException e) {
			throw new InvalidRequestException("올바른 포인트 값이 아닙니다: " + request.getNewValue());
		}
	}

	private void updateStatus(AdminUpdateRequest request) {
		AdminMemberDTO currentMember = adminMemberMapper.findByMemberNo(request.getMemberNo());
		if (currentMember == null) {
			throw new ResourceNotFoundException("회원을 찾을 수 없습니다.");
		}

		if (currentMember.getStatus().equals(request.getNewValue())) {
			throw new DuplicateException("요청이 이미 반영되어 있습니다.");
		}

		int result = adminMemberMapper.updateStatusByAdmin(
				request.getMemberNo(), request.getNewValue());

		// 상태 변경 시 토큰 삭제 (강제 로그아웃)
		tokenMapper.deleteToken(currentMember.getMemberId());

		if (result == 0) {
			throw new InvalidRequestException("회원 상태 변경 실패");
		}
	}

	private void updateRole(AdminUpdateRequest request) {
		AdminMemberDTO currentMember = adminMemberMapper.findByMemberNo(request.getMemberNo());
		if (currentMember == null) {
			throw new ResourceNotFoundException("회원을 찾을 수 없습니다.");
		}

		if (currentMember.getRole().equals(request.getNewValue())) {
			throw new DuplicateException("요청이 이미 반영되어 있습니다.");
		}

		int result = adminMemberMapper.updateRoleByAdmin(
				request.getMemberNo(), request.getNewValue());
		if (result == 0) {
			throw new InvalidRequestException("권한 변경 실패");
		}
	}

	private void updateName(AdminUpdateRequest request) {
		AdminMemberDTO currentMember = adminMemberMapper.findByMemberNo(request.getMemberNo());
		if (currentMember == null) {
			throw new ResourceNotFoundException("회원을 찾을 수 없습니다.");
		}

		if (currentMember.getMemberName().equals(request.getNewValue())) {
			throw new DuplicateException("요청이 이미 반영되어 있습니다.");
		}

		int result = adminMemberMapper.updateNameByAdmin(
				request.getMemberNo(), request.getNewValue());
		if (result == 0) {
			throw new InvalidRequestException("이름 변경 실패");
		}
	}
}
