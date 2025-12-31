package com.kh.eco.bookmark.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.board.model.service.BoardReportService;
import com.kh.eco.bookmark.model.dao.BookmarkMapper;
import com.kh.eco.bookmark.model.dto.BookmarkDTO;
import com.kh.eco.exception.InvalidRequestException;
import com.kh.eco.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 북마크 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkMapper bookmarkMapper;
    private final BoardReportService boardReportService;

    /**
     * 북마크 토글 (등록/해제)
     * @return true: 등록됨, false: 해제됨
     */
    @Override
    @Transactional
    public boolean toggleBookmark(BookmarkDTO bookmarkDTO) {
        // 로그인 여부 검증
        if (bookmarkDTO.getMemberNo() == 0) {
            throw UnauthorizedException.loginRequired();
        }

        // 게시글 존재 여부 검증
        boardReportService.selectBoardOne(bookmarkDTO.getBoardNo());

        // 북마크 존재 여부 확인
        boolean exists = bookmarkMapper.existsBookmark(bookmarkDTO) > 0;

        if (exists) {
            // 북마크 해제
            int result = bookmarkMapper.cancleBookmark(bookmarkDTO);
            if (result <= 0) {
                throw new InvalidRequestException("북마크 해제에 실패했습니다.");
            }
            log.info("북마크 해제 - memberNo: {}, boardNo: {}",
                    bookmarkDTO.getMemberNo(), bookmarkDTO.getBoardNo());
            return false;
        } else {
            // 북마크 등록
            int result = bookmarkMapper.insertBookmark(bookmarkDTO);
            if (result <= 0) {
                throw new InvalidRequestException("북마크 등록에 실패했습니다.");
            }
            log.info("북마크 등록 - memberNo: {}, boardNo: {}",
                    bookmarkDTO.getMemberNo(), bookmarkDTO.getBoardNo());
            return true;
        }
    }
}
