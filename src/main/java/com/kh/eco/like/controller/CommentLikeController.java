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

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentLikeController {

    private final LikeService likeService;

    @PostMapping("/{commentNo}/like")
    public ResponseEntity<ResponseData<LikeResponse>> commentLike(
            @PathVariable(name = "commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        int memberNo = userDetails.getMemberNo();
        LikeResponse response = likeService.commentLike(commentNo, memberNo);

        String message = response.isLiked() ? "좋아요가 등록되었습니다." : "좋아요가 취소되었습니다.";
        return ResponseData.ok(response, message);
    }
}
