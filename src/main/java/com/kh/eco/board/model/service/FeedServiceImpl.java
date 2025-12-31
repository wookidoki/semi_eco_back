package com.kh.eco.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.dao.FeedMapper;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.FeedBoardDTO;
import com.kh.eco.board.model.vo.BoardVO;
import com.kh.eco.exception.InvalidRequestException;
import com.kh.eco.exception.ResourceNotFoundException;
import com.kh.eco.exception.UnauthorizedException;
import com.kh.eco.file.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 피드 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final FeedMapper feedMapper;
    private final S3Service s3Service;
    private final BoardReportService boardReportService;

    /**
     * 피드 게시글 조회
     */
    @Override
    public List<FeedBoardDTO> selectFeedList(String category, Long fetchOffset, Long limit) {
        if ("C".equals(category)) {
            category = "C%";
        }
        return feedMapper.selectFeedList(category, fetchOffset, limit);
    }

    /**
     * 피드 게시글 조회 (오버로딩)
     */
    @Override
    public List<FeedBoardDTO> selectFeedList(Long boardNo) {
        return selectFeedList(null, null, boardNo);
    }

    /**
     * 게시글 작성
     * @return 업로드된 파일 경로 리스트
     */
    @Override
    @Transactional
    public List<String> insertFeed(FeedBoardDTO feed, List<MultipartFile> files) {
        log.info("피드 작성 - category: {}", feed.getBoardCategory());

        BoardVO board = BoardVO.builder()
                .refMno(feed.getBoardAuthor())
                .boardCategory(feed.getBoardCategory())
                .boardTitle(feed.getBoardTitle())
                .boardContent(feed.getBoardContent())
                .regDate(feed.getRegDate())
                .build();

        int result = feedMapper.insertFeed(board);
        if (result <= 0) {
            throw new InvalidRequestException("게시글 작성에 실패했습니다.");
        }

        // 파일 첨부 처리
        List<String> filePaths = new ArrayList<>();
        if (files != null) {
            filePaths = saveAttachments(board.getBoardNo(), files);
        }

        return filePaths;
    }

    /**
     * 게시글 삭제
     */
    @Override
    @Transactional
    public int deleteFeed(int boardNo, CustomUserDetails userDetails) {
        validateOwnership(boardNo, userDetails);
        boardReportService.selectBoardOne(boardNo);

        int result = feedMapper.deleteFeed(boardNo);
        if (result == 0) {
            throw new ResourceNotFoundException("게시글이 존재하지 않습니다.");
        }

        return result;
    }

    /**
     * 게시글 본인 여부 검증
     */
    @Override
    public int isOwner(int boardNo, CustomUserDetails userDetails) {
        int memberNo = userDetails.getMemberNo();
        int result = feedMapper.isOwner(boardNo, memberNo);

        if (result == 0) {
            throw UnauthorizedException.notOwner();
        }

        return result;
    }

    /**
     * 게시글 수정
     * @return 업로드된 파일 경로 리스트
     */
    @Override
    @Transactional
    public List<String> updateFeed(Long boardNo, FeedBoardDTO feed, List<MultipartFile> files, CustomUserDetails userDetails) {
        feed.setBoardAuthor(userDetails.getMemberNo());
        feed.setBoardNo(boardNo);

        int result = feedMapper.updateFeed(feed);
        if (result == 0) {
            throw new InvalidRequestException("게시글 수정에 실패했습니다.");
        }

        // 파일 처리
        List<String> filePaths = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            feedMapper.deleteAttachment(boardNo);
            filePaths = saveAttachments(boardNo, files);
        }

        return filePaths;
    }

    @Override
    public List<String> findAttachments(Long boardNo) {
        return feedMapper.selectAttachmentsByBoardNo(boardNo);
    }

    @Override
    public List<BoardDTO> selectPopularFeed(String category, Long fetchOffset, Long limit) {
        if ("C".equals(category)) {
            category = "C%";
        }
        return feedMapper.selectPopularFeed(category, fetchOffset, limit);
    }

    /**
     * 본인 여부 검증 (private)
     */
    private void validateOwnership(int boardNo, CustomUserDetails userDetails) {
        int memberNo = userDetails.getMemberNo();
        int result = feedMapper.isOwner(boardNo, memberNo);
        if (result == 0) {
            throw UnauthorizedException.notOwner();
        }
    }

    /**
     * 첨부파일 저장 (S3)
     * @return 업로드된 파일 경로 리스트
     */
    private List<String> saveAttachments(Long boardNo, List<MultipartFile> files) {
        List<String> filePaths = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String fileUrl = s3Service.upload(file, "feed");
                String originName = file.getOriginalFilename();

                Map<String, Object> fileMap = new HashMap<>();
                fileMap.put("refBno", boardNo);
                fileMap.put("originName", originName);
                fileMap.put("changeName", extractFileName(fileUrl));
                fileMap.put("attachmentPath", fileUrl);

                int saveResult = feedMapper.saveAttachment(fileMap);
                if (saveResult <= 0) {
                    log.warn("첨부파일 저장 실패 - boardNo: {}, fileName: {}", boardNo, originName);
                } else {
                    filePaths.add(fileUrl);
                }
            }
        }

        return filePaths;
    }

    /**
     * S3 URL에서 파일명 추출
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null) return null;
        int lastSlash = fileUrl.lastIndexOf('/');
        return lastSlash >= 0 ? fileUrl.substring(lastSlash + 1) : fileUrl;
    }
}
