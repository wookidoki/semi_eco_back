package com.kh.eco.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.bookmark.model.dto.BookmarkDTO;
import com.kh.eco.bookmark.model.service.BookmarkService;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 북마크 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"api/boards", "api/feeds"})
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 북마크 토글 (등록/해제)
     */
    @PostMapping("/{boardNo}/bookmark")
    public ResponseEntity<ResponseData<BookmarkResponse>> toggleBookmark(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "게시글이 존재하지 않습니다.") int boardNo,
            @RequestBody BookmarkDTO bookmarkDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        bookmarkDTO.setMemberNo(userDetails.getMemberNo());
        bookmarkDTO.setBoardNo(boardNo);

        boolean isBookmarked = bookmarkService.toggleBookmark(bookmarkDTO);

        String message = isBookmarked ? "북마크가 등록되었습니다." : "북마크가 해제되었습니다.";
        BookmarkResponse response = new BookmarkResponse(isBookmarked);

        return ResponseData.ok(response, message);
    }

    /**
     * 북마크 응답 DTO
     */
    public record BookmarkResponse(boolean isBookmarked) {}
}
