package com.kh.eco.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.dto.BoardReportDTO;
import com.kh.eco.board.model.service.BoardReportService;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/boards")
public class BoardReportController {

    private final BoardReportService boardReportService;

    @PostMapping("/{boardNo}/reports")
    public ResponseEntity<ResponseData<Object>> boardReport(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "잘못된 접근입니다.") int boardNo,
            @RequestBody @NotNull(message = "잘못된 접근입니다.") BoardReportDTO reportDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Object result = boardReportService.boardReport(boardNo, reportDTO);
        return ResponseData.created(result, "게시글이 신고되었습니다.");
    }
}
