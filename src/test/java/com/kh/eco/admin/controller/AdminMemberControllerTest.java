package com.kh.eco.admin.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.eco.admin.model.dto.AdminMemberDTO;
import com.kh.eco.admin.model.service.AdminMemberService;
import com.kh.eco.common.dto.AdminUpdateRequest;

/**
 * AdminMemberController 통합 테스트
 */
@WebMvcTest(AdminMemberController.class)
class AdminMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminMemberService adminMemberService;

    private AdminMemberDTO testMember;

    @BeforeEach
    void setUp() {
        testMember = new AdminMemberDTO();
        testMember.setMemberNo(1);
        testMember.setMemberId("testUser");
        testMember.setMemberName("테*트");
        testMember.setEmail("te**@test.com");
        testMember.setPhone("010-****-5678");
        testMember.setStatus("Y");
        testMember.setRole("USER");
    }

    @Nested
    @DisplayName("회원 조회 API")
    class FindMemberApiTest {

        @Test
        @DisplayName("성공: 회원 조회")
        @WithMockUser(roles = "ADMIN")
        void findMember_Success() throws Exception {
            // given
            given(adminMemberService.findMemberById("testUser")).willReturn(testMember);

            // when & then
            mockMvc.perform(get("/admin/members")
                            .param("keyword", "testUser"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.memberId").value("testUser"));
        }

        @Test
        @DisplayName("실패: 권한 없음")
        @WithMockUser(roles = "USER")
        void findMember_Forbidden() throws Exception {
            // when & then
            mockMvc.perform(get("/admin/members")
                            .param("keyword", "testUser"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("회원 정보 수정 API")
    class UpdateMemberApiTest {

        @Test
        @DisplayName("성공: 이메일 변경")
        @WithMockUser(roles = "ADMIN")
        void updateEmail_Success() throws Exception {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    .field("email")
                    .newValue("new@test.com")
                    .build();

            willDoNothing().given(adminMemberService).updateMemberField(any(AdminUpdateRequest.class));

            // when & then
            mockMvc.perform(put("/admin/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("이메일 변경 성공"));
        }

        @Test
        @DisplayName("실패: 필수 필드 누락")
        @WithMockUser(roles = "ADMIN")
        void updateMember_ValidationFail() throws Exception {
            // given
            AdminUpdateRequest request = AdminUpdateRequest.builder()
                    .memberNo(1)
                    // field와 newValue 누락
                    .build();

            // when & then
            mockMvc.perform(put("/admin/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
