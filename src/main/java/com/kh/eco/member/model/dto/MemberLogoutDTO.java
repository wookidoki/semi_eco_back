package com.kh.eco.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그아웃 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLogoutDTO {

    @NotBlank(message = "회원 ID는 필수입니다.")
    private String memberId;

    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    private String refreshToken;
}
