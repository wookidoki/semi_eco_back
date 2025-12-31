package com.kh.eco.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.admin.model.dto.AdminCommentDTO;
import com.kh.eco.admin.model.dto.CommentPageResponse;
import com.kh.eco.admin.model.service.AdminCommentService;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    /**
     * 댓글 전체 조회
     */
    @GetMapping
    public ResponseEntity<ResponseData<CommentPageResponse>> findCommentAll(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "잘못된 접근입니다.") int pageNo) {

        CommentPageResponse result = adminCommentService.findCommentAll(pageNo);
        return ResponseData.ok(result);
    }

    /**
     * 신고된 댓글만 조회
     */
    @GetMapping("reported")
    public ResponseEntity<ResponseData<CommentPageResponse>> findReportedComment(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "잘못된 접근입니다.") int pageNo) {

        CommentPageResponse result = adminCommentService.findReportedComment(pageNo);
        return ResponseData.ok(result);
    }

    /**
     * 댓글 상세 조회
     */
    @GetMapping("{commentNo}")
    public ResponseEntity<ResponseData<AdminCommentDTO>> selectComment(
            @PathVariable(name = "commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo) {

        AdminCommentDTO comment = adminCommentService.findByCommentNo(commentNo);
        return ResponseData.ok(comment);
    }

    /**
     * 댓글 삭제 (비공개 처리)
     */
    @DeleteMapping("{commentNo}")
    public ResponseEntity<ResponseData<Void>> deleteComment(
            @PathVariable(name = "commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo) {

        adminCommentService.deleteComment(commentNo);
        return ResponseData.ok("댓글이 비공개처리 되었습니다.");
    }

    /**
     * 댓글 복원
     */
    @PutMapping("{commentNo}")
    public ResponseEntity<ResponseData<Void>> restoreComment(
            @PathVariable(name = "commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo) {

        adminCommentService.restoreComment(commentNo);
        return ResponseData.ok("댓글이 복원 되었습니다.");
    }

    /**
     * 댓글 신고 확인 처리
     */
    @PutMapping("report/{commentNo}")
    public ResponseEntity<ResponseData<Void>> handleReport(
            @PathVariable(name = "commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo) {

        adminCommentService.handleReport(commentNo);
        return ResponseData.ok("신고 확인 완료.");
    }
}
