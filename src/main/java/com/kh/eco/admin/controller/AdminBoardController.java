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

import com.kh.eco.admin.model.dto.AdminBoardDetailDTO;
import com.kh.eco.admin.model.dto.PageResponse;
import com.kh.eco.admin.model.service.AdminBoardService;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("api/admin/boards")
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    /**
     * 게시글 전체 조회
     */
    @GetMapping
    public ResponseEntity<ResponseData<PageResponse>> findBoardAll(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "잘못된 접근입니다.") int pageNo) {

        PageResponse result = adminBoardService.findBoardAll(pageNo);
        return ResponseData.ok(result);
    }

    /**
     * 신고된 게시글만 조회
     */
    @GetMapping("reported")
    public ResponseEntity<ResponseData<PageResponse>> findReportedBoard(
            @RequestParam(name = "page", defaultValue = "0") @Min(value = 0, message = "잘못된 접근입니다.") int pageNo) {

        PageResponse result = adminBoardService.findReportedBoard(pageNo);
        return ResponseData.ok(result);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("{boardNo}")
    public ResponseEntity<ResponseData<AdminBoardDetailDTO>> selectBoard(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "잘못된 접근입니다.") Long boardNo) {

        AdminBoardDetailDTO board = adminBoardService.findByBoardNo(boardNo);
        return ResponseData.ok(board);
    }

    /**
     * 게시글 삭제 (비공개 처리)
     */
    @DeleteMapping("{boardNo}")
    public ResponseEntity<ResponseData<Void>> deleteBoard(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "잘못된 접근입니다.") Long boardNo) {

        adminBoardService.deleteBoard(boardNo);
        return ResponseData.ok("게시글이 비공개처리 되었습니다.");
    }

    /**
     * 게시글 복원
     */
    @PutMapping("{boardNo}")
    public ResponseEntity<ResponseData<Void>> restoreBoard(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "잘못된 접근입니다.") Long boardNo) {

        adminBoardService.restoreBoard(boardNo);
        return ResponseData.ok("게시글이 복원 되었습니다.");
    }

    /**
     * 게시글 신고 확인 처리
     */
    @PutMapping("report/{boardNo}")
    public ResponseEntity<ResponseData<Void>> handleReport(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "잘못된 접근입니다.") Long boardNo) {

        adminBoardService.handleReport(boardNo);
        return ResponseData.ok("신고 확인 완료.");
    }
}
