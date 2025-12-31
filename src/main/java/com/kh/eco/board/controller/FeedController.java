package com.kh.eco.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.FeedBoardDTO;
import com.kh.eco.board.model.service.FeedService;
import com.kh.eco.common.dto.ResponseData;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/feeds")
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<ResponseData<List<FeedBoardDTO>>> selectFeedList(
            @RequestParam(name = "category", defaultValue = "C") String category,
            @RequestParam(name = "fetchOffset", required = false) Long fetchOffset,
            @RequestParam(name = "limit", defaultValue = "10") Long limit) {

        log.info("GET /feeds 요청 - category={}, fetchOffset={}, limit={}",
                category, fetchOffset, limit);

        List<FeedBoardDTO> feeds = feedService.selectFeedList(category, fetchOffset, limit);
       
        return ResponseData.ok(feeds);
    }

    /**
     * 피드 게시글 작성
     */
    @PostMapping
    public ResponseEntity<ResponseData<Void>> insertFeed(
            @Valid FeedBoardDTO feed,
            @RequestParam(name = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        int memberNo = userDetails.getMemberNo();
        feed.setBoardAuthor(memberNo);

        feedService.insertFeed(feed, files);
        return ResponseData.created(null, "피드가 작성되었습니다.");
    }

    @DeleteMapping("/{boardNo}/delete")
    public ResponseEntity<ResponseData<Void>> deleteFeed(
            @PathVariable(name = "boardNo") @Min(value = 1, message = "게시글이 존재하지 않습니다.") int boardNo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        feedService.deleteFeed(boardNo, userDetails);
        return ResponseData.ok("게시글이 삭제되었습니다.");
    }

    @PutMapping("/{boardNo}")
    public ResponseEntity<ResponseData<Void>> updateFeed(
            @PathVariable(name = "boardNo") Long boardNo,
            @Valid FeedBoardDTO feed,
            @RequestParam(name = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        feedService.updateFeed(boardNo, feed, files, userDetails);
        return ResponseData.ok("피드가 수정되었습니다.");
    }

    @GetMapping("/{boardNo}/attachments")
    public ResponseEntity<ResponseData<List<String>>> findAttachments(
            @PathVariable(name = "boardNo") Long boardNo) {

        List<String> attachments = feedService.findAttachments(boardNo);
        return ResponseData.ok(attachments);
    }

    @GetMapping("/popular")
    public ResponseEntity<ResponseData<List<BoardDTO>>> selectPopularFeed(
            @RequestParam(name = "category", defaultValue = "C") String category,
            @RequestParam(name = "fetchOffset", required = false) Long fetchOffset,
            @RequestParam(name = "limit", defaultValue = "10") Long limit) {

        List<BoardDTO> popularFeeds = feedService.selectPopularFeed(category, fetchOffset, limit);
        return ResponseData.ok(popularFeeds);
    }
}
