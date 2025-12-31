package com.kh.eco.like.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.common.dto.ResponseData;
import com.kh.eco.like.model.service.LikeService;
import com.kh.eco.like.model.vo.LikeResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"api/boards", "api/feeds"})
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{boardNo}/like")
    public ResponseEntity<ResponseData<LikeResponse>> toggleLike(
            @PathVariable("boardNo") Long boardNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        int memberNo = userDetails.getMemberNo();
        LikeResponse response = likeService.toggleLike(boardNo, memberNo);

        String message = response.isLiked() ? "좋아요가 등록되었습니다." : "좋아요가 취소되었습니다.";
        return ResponseData.ok(response, message);
    }
}
