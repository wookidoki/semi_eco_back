package com.kh.eco.admin.model.service;

import com.kh.eco.admin.model.dto.AdminMemberDTO;
import com.kh.eco.common.dto.AdminUpdateRequest;

/**
 * 관리자 회원 관리 서비스 인터페이스
 */
public interface AdminMemberService {

	/**
	 * 회원 ID로 회원 정보 조회
	 */
	AdminMemberDTO findMemberById(String memberId);

	/**
	 * 회원 정보 통합 수정
	 * 필드 타입에 따라 적절한 수정 로직 실행
	 */
	void updateMemberField(AdminUpdateRequest request);
}
