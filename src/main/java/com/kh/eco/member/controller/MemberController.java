package com.kh.eco.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.common.dto.ResponseData;
import com.kh.eco.member.model.dto.ChangePasswordDTO;
import com.kh.eco.member.model.dto.MemberSignUpDTO;
import com.kh.eco.member.model.dto.UpdateEmailDTO;
import com.kh.eco.member.model.dto.UpdatePhoneDTO;
import com.kh.eco.member.model.dto.UpdateProfileDTO;
import com.kh.eco.member.model.dto.UpdateRegionDTO;
import com.kh.eco.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ResponseData<Void>> signUp(
            @Valid MemberSignUpDTO member,
            @RequestParam(name = "profileImg", required = false) MultipartFile profileImg) {

        log.info("회원가입 요청 - 회원정보: {}", member);

        if (profileImg != null && !profileImg.isEmpty()) {
            log.debug("프로필 이미지: {} ({}bytes)",
                    profileImg.getOriginalFilename(),
                    profileImg.getSize());
        }

        memberService.signUp(member, profileImg);
        return ResponseData.created(null, "회원가입이 완료되었습니다.");
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("password")
    public ResponseEntity<ResponseData<Void>> changePassword(
            @Valid @RequestBody ChangePasswordDTO password) {

        log.debug("비밀번호 변경 요청");
        memberService.changePassword(password);
        return ResponseData.ok("비밀번호가 변경되었습니다.");
    }

    /**
     * 이메일 변경
     */
    @PutMapping("email")
    public ResponseEntity<ResponseData<Void>> changeEmail(
            @Valid @RequestBody UpdateEmailDTO email) {

        log.debug("이메일 변경 요청: {}", email);
        memberService.updateMemberEmail(email);
        return ResponseData.ok("이메일이 변경되었습니다.");
    }

    /**
     * 전화번호 변경
     */
    @PutMapping("phone")
    public ResponseEntity<ResponseData<Void>> changePhone(
            @Valid @RequestBody UpdatePhoneDTO phone) {

        memberService.updateMemberPhone(phone);
        return ResponseData.ok("번호가 변경되었습니다.");
    }

    /**
     * 프로필 이미지 변경
     */
    @PostMapping("profile")
    public ResponseEntity<ResponseData<Void>> updateProfileImage(
            @ModelAttribute UpdateProfileDTO profile) {

        log.debug("프로필 업데이트 요청 - memberId: {}, hasImage: {}",
                profile.getMemberId(),
                profile.getNewImage() != null && !profile.getNewImage().isEmpty());

        String newImagePath = memberService.updateProfile(profile);

        return ResponseData.okWithFile(newImagePath, "프로필 이미지가 변경되었습니다.");
    }

    /**
     * 지역 변경
     */
    @PutMapping("region")
    public ResponseEntity<ResponseData<Void>> changeRegion(
            @Valid @RequestBody UpdateRegionDTO region) {

        memberService.updateMemberRegion(region);
        return ResponseData.ok("지역이 변경되었습니다.");
    }

    @GetMapping("/posts")
    public ResponseEntity<ResponseData<Map<String, Object>>> getMyPosts(
            @RequestParam(value = "memberNo") long memberNo,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Map<String, Object> response = memberService.getMyPosts(memberNo, page);
        return ResponseData.ok(response);
    }

    @GetMapping("/comments")
    public ResponseEntity<ResponseData<Map<String, Object>>> getMyComments(
            @RequestParam(value = "memberNo") long memberNo,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Map<String, Object> response = memberService.getMyComments(memberNo, page);
        return ResponseData.ok(response);
    }

    @GetMapping("/likes")
    public ResponseEntity<ResponseData<Map<String, Object>>> getMyLikes(
            @RequestParam(value = "memberNo") long memberNo,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Map<String, Object> response = memberService.getMyLikes(memberNo, page);
        return ResponseData.ok(response);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<ResponseData<Map<String, Object>>> getMyBookmarks(
            @RequestParam(value = "memberNo") long memberNo,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        Map<String, Object> response = memberService.getMyBookmarks(memberNo, page);
        return ResponseData.ok(response);
    }

}
