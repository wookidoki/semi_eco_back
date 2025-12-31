package com.kh.eco.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.comment.model.dto.CommentDTO;
import com.kh.eco.comment.model.dto.CommentReportDTO;
import com.kh.eco.comment.model.dto.CommentUpdateDTO;
import com.kh.eco.comment.model.service.CommentService;
import com.kh.eco.comment.model.vo.CommentVO;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity<ResponseData<CommentVO>> insertComment(
            @RequestBody CommentDTO comment,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        comment.setRefMno(userDetails.getMemberNo());
        CommentVO c = commentService.insertComment(comment, userDetails);

        return ResponseData.created(c, "댓글이 작성되었습니다.");
    }

    /**
     * 댓글 조회
     */
    @GetMapping
    public ResponseEntity<ResponseData<List<CommentDTO>>> findAll(
            @RequestParam(name = "boardNo") @Min(value = 1, message = "잘못된 접근입니다.") Long boardNo) {

        List<CommentDTO> comments = commentService.findAll(boardNo);
        return ResponseData.ok(comments);
    }

    /**
     * 댓글 신고
     */
    @PostMapping("/{commentNo}/reports")
    public ResponseEntity<ResponseData<Object>> commentReport(
            @PathVariable("commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo,
            @RequestBody @NotNull(message = "잘못된 접근입니다.") CommentReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.existById(commentNo);
        reportDTO.setRefCno(commentNo);

        Object result = commentService.commentReport(reportDTO);
        return ResponseData.created(result, "댓글이 신고되었습니다.");
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<ResponseData<Void>> deleteComment(
            @PathVariable("commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.existById(commentNo);
        Integer mno = userDetails.getMemberNo();
        commentService.isOwner(commentNo, mno);
        commentService.deleteComment(commentNo);

        return ResponseData.ok("댓글이 삭제되었습니다.");
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{commentNo}")
    public ResponseEntity<ResponseData<Void>> updateComment(
            @PathVariable(name = "commentNo") @Min(value = 1, message = "잘못된 접근입니다.") Long commentNo,
            @RequestBody @Valid CommentUpdateDTO updateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        commentService.existById(commentNo);
        Integer mno = userDetails.getMemberNo();
        commentService.isOwner(commentNo, mno);

        CommentDTO comment = new CommentDTO();
        comment.setCommentNo(commentNo);
        comment.setCommentContent(updateDTO.getCommentContent());

        commentService.updateComment(comment);
        return ResponseData.ok("댓글이 수정되었습니다.");
    }
}
