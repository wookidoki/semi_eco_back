package com.kh.eco.admin.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kh.eco.admin.model.dao.AdminMemberMapper;
import com.kh.eco.admin.model.dto.AdminMemberDTO;
import com.kh.eco.admin.model.service.AdminMemberServiceImpl;
import com.kh.eco.common.dto.AdminUpdateRequest;
import com.kh.eco.exception.DuplicateException;
import com.kh.eco.exception.InvalidRequestException;
import com.kh.eco.exception.ResourceNotFoundException;
import com.kh.eco.member.model.service.MemberInfoDuplicateCheck;
import com.kh.eco.token.model.dao.TokenMapper;

/**
 * AdminMemberService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class AdminMemberServiceTest {

    @InjectMocks
    private AdminMemberServiceImpl adminMemberService;

    @Mock
    private AdminMemberMapper adminMemberMapper;

    @Mock
    private MemberInfoDuplicateCheck duplicateCheck;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenMapper tokenMapper;

    private AdminMemberDTO testMember;

    @BeforeEach
    void setUp() {
        testMember = new AdminMemberDTO();
        testMember.setMemberNo(1);
        testMember.setMemberId("testUser");
        testMember.setMemberName("테스트");
        testMember.setEmail("test@test.com");
        testMember.setPhone("010-1234-5678");
        testMember.setStatus("Y");
        testMember.setRole("USER");
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class FindMemberTest {

        @Test
        @DisplayName("성공: 회원 ID로 조회")
        void findMemberById_Success() {
            // given
            given(adminMemberMapper.findMemberById("testUser")).willReturn(testMember);

            // when
            AdminMemberDTO result = adminMemberService.findMemberById("testUser");

            // then
            assertThat(result).isNotNull();
            assertThat(result.getMemberId()).isEqualTo("testUser");
            then(adminMemberMapper).should().findMemberById("testUser");
        }

        @Test
        @DisplayName("실패: 존재하지 않는 회원")
        void findMemberById_NotFound() {
            // given
            given(adminMemberMapper.findMemberById("notExist")).willReturn(null);

            // when & then
            assertThatThrownBy(() -> adminMemberService.findMemberById("notExist"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 테스트")
    class UpdateMemberTest {

        @Test
        @DisplayName("성공: 이메일 변경")
        void updateEmail_Success() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("email")
                    .newValue("new@test.com")
                    .build();

            willDoNothing().given(duplicateCheck).emailDuplicateCheck("new@test.com");
            given(adminMemberMapper.updateEmailByAdmin(1, "new@test.com")).willReturn(1);

            // when
            adminMemberService.updateMemberField(request);

            // then
            then(duplicateCheck).should().emailDuplicateCheck("new@test.com");
            then(adminMemberMapper).should().updateEmailByAdmin(1, "new@test.com");
        }

        @Test
        @DisplayName("성공: 비밀번호 변경")
        void updatePassword_Success() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("password")
                    .newValue("newPassword123")
                    .build();

            given(passwordEncoder.encode("newPassword123")).willReturn("encodedPassword");
            given(adminMemberMapper.updatePasswordByAdmin(1, "encodedPassword")).willReturn(1);

            // when
            adminMemberService.updateMemberField(request);

            // then
            then(passwordEncoder).should().encode("newPassword123");
            then(adminMemberMapper).should().updatePasswordByAdmin(1, "encodedPassword");
        }

        @Test
        @DisplayName("성공: 회원 상태 변경")
        void updateStatus_Success() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("status")
                    .newValue("N")
                    .build();

            given(adminMemberMapper.findByMemberNo(1)).willReturn(testMember);
            given(adminMemberMapper.updateStatusByAdmin(1, "N")).willReturn(1);

            // when
            adminMemberService.updateMemberField(request);

            // then
            then(adminMemberMapper).should().updateStatusByAdmin(1, "N");
            then(tokenMapper).should().deleteToken("testUser"); // 상태 변경 시 토큰 삭제
        }

        @Test
        @DisplayName("실패: 동일한 상태로 변경 시도")
        void updateStatus_SameValue() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("status")
                    .newValue("Y") // 현재 상태와 동일
                    .build();

            given(adminMemberMapper.findByMemberNo(1)).willReturn(testMember);

            // when & then
            assertThatThrownBy(() -> adminMemberService.updateMemberField(request))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessage("요청이 이미 반영되어 있습니다.");
        }

        @Test
        @DisplayName("실패: 지원하지 않는 필드")
        void updateField_UnsupportedField() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("unsupported")
                    .newValue("value")
                    .build();

            // when & then
            assertThatThrownBy(() -> adminMemberService.updateMemberField(request))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("지원하지 않는 필드");
        }

        @Test
        @DisplayName("성공: 지역 변경")
        void updateRegion_Success() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("region")
                    .newValue("서울")
                    .build();

            given(adminMemberMapper.findRegionNoByName("서울")).willReturn(1);
            given(adminMemberMapper.updateRegionByAdmin(1, 1)).willReturn(1);

            // when
            adminMemberService.updateMemberField(request);

            // then
            then(adminMemberMapper).should().findRegionNoByName("서울");
            then(adminMemberMapper).should().updateRegionByAdmin(1, 1);
        }

        @Test
        @DisplayName("실패: 등록되지 않은 지역")
        void updateRegion_InvalidRegion() {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("region")
                    .newValue("없는지역")
                    .build();

            given(adminMemberMapper.findRegionNoByName("없는지역")).willReturn(null);

            // when & then
            assertThatThrownBy(() -> adminMemberService.updateMemberField(request))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("등록된 지역명이 아닙니다");
        }
    }
}
