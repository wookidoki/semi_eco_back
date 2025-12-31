package com.kh.eco.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.kh.eco.auth.model.service.AuthServiceImpl;
import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.exception.CustomAuthenticationException;
import com.kh.eco.member.model.dto.MemberLoginDTO;
import com.kh.eco.token.model.service.TokenService;

/**
 * AuthService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails customUserDetails;

    private MemberLoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        loginDTO = new MemberLoginDTO();
        loginDTO.setMemberId("testUser");
        loginDTO.setMemberPwd("password123");
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        @Test
        @DisplayName("성공: 유효한 자격 증명으로 로그인")
        void login_Success() {
            // given
            given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .willReturn(authentication);
            given(authentication.getPrincipal()).willReturn(customUserDetails);
            given(customUserDetails.getUsername()).willReturn("testUser");
            given(customUserDetails.getMemberName()).willReturn("테스트유저");
            given(customUserDetails.getMemberNo()).willReturn(1);
            given(customUserDetails.getPhone()).willReturn("010-1234-5678");
            given(customUserDetails.getEmail()).willReturn("test@test.com");
            given(customUserDetails.getRefRno()).willReturn(1);
            given(customUserDetails.getMemberImage()).willReturn(null);
            //given(customUserDetails.getMemberPoint()).willReturn(0L);
            given(customUserDetails.getEnrollDate()).willReturn(new java.sql.Date(System.currentTimeMillis()));

            Map<String, String> tokenResponse = new HashMap<>();
            tokenResponse.put("accessToken", "access-token");
            tokenResponse.put("refreshToken", "refresh-token");
            given(tokenService.generateToken("testUser")).willReturn(tokenResponse);

            // when
            Map<String, String> result = authService.login(loginDTO);

            // then
            assertThat(result).isNotNull();
            assertThat(result.get("memberId")).isEqualTo("testUser");
            assertThat(result.get("memberName")).isEqualTo("테스트유저");
            assertThat(result.get("accessToken")).isEqualTo("access-token");

            then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
            then(tokenService).should().generateToken("testUser");
        }

        @Test
        @DisplayName("실패: 잘못된 비밀번호")
        void login_Fail_WrongPassword() {
            // given
            given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .willThrow(new BadCredentialsException("Bad credentials"));

            // when & then
            assertThatThrownBy(() -> authService.login(loginDTO))
                    .isInstanceOf(CustomAuthenticationException.class)
                    .hasMessage("비밀번호 미일치");
        }
    }
}
