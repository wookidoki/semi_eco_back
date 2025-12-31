package com.kh.eco.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.admin.model.dto.AdminMemberDTO;
import com.kh.eco.admin.model.service.AdminMemberService;
import com.kh.eco.common.dto.AdminUpdateRequest;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 회원 관리 컨트롤러
 */
@Slf4j
@RestController
@Validated
@RequestMapping("api/admin/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    /**
     * 회원 정보 조회
     */
    @GetMapping
    public ResponseEntity<ResponseData<AdminMemberDTO>> findMemberById(
            @RequestParam(value = "keyword") @Valid String memberId) {

        AdminMemberDTO member = adminMemberService.findMemberById(memberId);
        return ResponseData.ok(member);
    }

    /**
     * 회원 정보 통합 수정
     */
    @PutMapping
    public ResponseEntity<ResponseData<Void>> updateMemberField(
            @RequestBody @Valid AdminUpdateRequest request) {

        log.info("회원 정보 수정 요청 - memberNo: {}, field: {}",
                request.getMemberNo(), request.getField());

        adminMemberService.updateMemberField(request);

        String message = getSuccessMessage(request.getField());
        return ResponseData.ok(message);
    }

    private String getSuccessMessage(String field) {
        return switch (field.toLowerCase()) {
            case "id" -> "아이디 변경 성공";
            case "password" -> "비밀번호 변경 성공";
            case "phone" -> "전화번호 변경 성공";
            case "email" -> "이메일 변경 성공";
            case "region" -> "지역 변경 성공";
            case "point" -> "포인트 변경 성공";
            case "status" -> "회원 상태 변경 성공";
            case "role" -> "권한 변경 성공";
            case "name" -> "이름 변경 성공";
            default -> "회원 정보 변경 성공";
        };
    }
}
