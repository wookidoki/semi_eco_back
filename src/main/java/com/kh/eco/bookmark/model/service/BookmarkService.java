package com.kh.eco.bookmark.model.service;

import com.kh.eco.bookmark.model.dto.BookmarkDTO;

/**
 * 북마크 서비스 인터페이스
 */
public interface BookmarkService {

    /**
     * 북마크 토글 (등록/해제)
     * @param bookmarkDTO 북마크 정보
     * @return true: 등록됨, false: 해제됨
     */
    boolean toggleBookmark(BookmarkDTO bookmarkDTO);
}
