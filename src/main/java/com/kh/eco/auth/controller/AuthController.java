package com.kh.eco.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.auth.model.service.AuthService;
import com.kh.eco.common.dto.ResponseData;
import com.kh.eco.member.model.dto.MemberLoginDTO;
import com.kh.eco.member.model.dto.MemberLogoutDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<Map<String, String>>> login(
            @Valid @RequestBody MemberLoginDTO member) {

        Map<String, String> loginResponse = authService.login(member);
        return ResponseData.ok(loginResponse, "로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<Void>> logout(
            @Valid @RequestBody MemberLogoutDTO member) {

        authService.logout(member);
        return ResponseData.ok("로그아웃 완료");
    }
}
