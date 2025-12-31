package com.kh.eco.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.BoardDetailDTO;
import com.kh.eco.board.model.service.BoardService;
import com.kh.eco.common.dto.ResponseData;
import com.kh.eco.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 게시글 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록 조회
     */
    @GetMapping
    public ResponseEntity<ResponseData<Map<String, Object>>> selectBoardList(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword) {

        log.info("목록 조회 요청 - page: {}, type: {}, category: {}", currentPage, type, category);

        Map<String, Object> params = new HashMap<>();
        params.put("currentPage", currentPage);

        if (type != null) params.put("type", type);
        if (category != null && !"ALL".equals(category)) params.put("category", category);
        if (keyword != null) params.put("keyword", keyword);

        Map<String, Object> responseData = boardService.selectBoardList(params);
        return ResponseData.ok(responseData);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{boardNo}")
    public ResponseEntity<ResponseData<BoardDetailDTO>> selectBoardDetail(
            @PathVariable("boardNo") Long boardNo) {

        BoardDetailDTO board = boardService.selectBoardDetail(boardNo);
        if (board == null) {
            throw ResourceNotFoundException.board(boardNo);
        }
        return ResponseData.ok(board);
    }

    /**
     * 게시글 작성
     */
    @PostMapping
    public ResponseEntity<ResponseData<Void>> insertBoard(
            @RequestPart("board") @Valid BoardDTO board,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails user) {

        log.info("게시글 작성 요청 - user: {}", user.getUsername());

        String filePath = boardService.insertBoard(board, file, user.getUsername());

        if (filePath != null) {
            return ResponseData.createdWithFile(filePath, "게시글이 작성되었습니다.");
        }
        return ResponseData.created(null, "게시글이 작성되었습니다.");
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{boardNo}")
    public ResponseEntity<ResponseData<Void>> updateBoard(
            @PathVariable("boardNo") Long boardNo,
            @RequestPart("board") @Valid BoardDTO board,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails user) {

        log.info("게시글 수정 요청 - boardNo: {}, user: {}", boardNo, user.getUsername());

        board.setBoardNo(boardNo);
        String filePath = boardService.updateBoard(board, file, user.getUsername());

        if (filePath != null) {
            return ResponseData.okWithFile(filePath, "게시글이 수정되었습니다.");
        }
        return ResponseData.ok("게시글이 수정되었습니다.");
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ResponseData<Void>> deleteBoard(
            @PathVariable("boardNo") Long boardNo,
            @AuthenticationPrincipal CustomUserDetails user) {

        log.info("게시글 삭제 요청 - boardNo: {}, user: {}", boardNo, user.getUsername());

        boardService.deleteBoard(boardNo, user.getUsername());
        return ResponseData.ok("게시글이 삭제되었습니다.");
    }
}
