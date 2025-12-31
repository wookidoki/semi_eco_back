package com.kh.eco.admin.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.eco.admin.model.dto.AdminMemberDTO;

/**
 * 관리자 회원 관리 매퍼
 */
@Mapper
public interface AdminMemberMapper {

	/**
	 * 아이디로 회원 정보 조회
	 */
	AdminMemberDTO findMemberById(String memberId);

	/**
	 * 회원 번호로 회원 정보 조회
	 */
	AdminMemberDTO findByMemberNo(@Param("memberNo") Integer memberNo);

	/**
	 * 지역명으로 지역 번호 조회
	 */
	Integer findRegionNoByName(@Param("regionName") String regionName);

	/**
	 * 아이디 변경
	 */
	int updateMemberIdByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newId") String newId);

	/**
	 * 비밀번호 변경
	 */
	int updatePasswordByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newPwd") String newPwd);

	/**
	 * 전화번호 변경
	 */
	int updatePhoneByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newPhone") String newPhone);

	/**
	 * 이메일 변경
	 */
	int updateEmailByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newEmail") String newEmail);

	/**
	 * 지역 변경
	 */
	int updateRegionByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("regionNo") Integer regionNo);

	/**
	 * 포인트 변경
	 */
	int updatePointByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newPoint") Long newPoint);

	/**
	 * 회원 상태 변경
	 */
	int updateStatusByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newStatus") String newStatus);

	/**
	 * 권한 변경
	 */
	int updateRoleByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newRole") String newRole);

	/**
	 * 이름 변경
	 */
	int updateNameByAdmin(
			@Param("memberNo") Integer memberNo,
			@Param("newName") String newName);
}
