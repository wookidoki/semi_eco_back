package com.kh.eco.bookmark.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kh.eco.board.model.service.BoardReportService;
import com.kh.eco.bookmark.model.dao.BookmarkMapper;
import com.kh.eco.bookmark.model.dto.BookmarkDTO;
import com.kh.eco.bookmark.model.service.BookmarkServiceImpl;
import com.kh.eco.exception.InvalidRequestException;
import com.kh.eco.exception.UnauthorizedException;

/**
 * BookmarkService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    private BookmarkServiceImpl bookmarkService;

    @Mock
    private BookmarkMapper bookmarkMapper;

    @Mock
    private BoardReportService boardReportService;

    private BookmarkDTO bookmarkDTO;

    @BeforeEach
    void setUp() {
        bookmarkDTO = new BookmarkDTO();
        bookmarkDTO.setMemberNo(1);
        bookmarkDTO.setBoardNo(100);
    }

    @Nested
    @DisplayName("북마크 토글 테스트")
    class ToggleBookmarkTest {

        @Test
        @DisplayName("성공: 북마크 등록")
        void toggleBookmark_Add() {
            // given
            given(bookmarkMapper.existsBookmark(bookmarkDTO)).willReturn(0); // 북마크 없음
            given(bookmarkMapper.insertBookmark(bookmarkDTO)).willReturn(1);

            // when
            boolean result = bookmarkService.toggleBookmark(bookmarkDTO);

            // then
            assertThat(result).isTrue(); // 북마크 등록됨
            then(bookmarkMapper).should().insertBookmark(bookmarkDTO);
            then(bookmarkMapper).should(never()).cancleBookmark(any());
        }

        @Test
        @DisplayName("성공: 북마크 해제")
        void toggleBookmark_Remove() {
            // given
            given(bookmarkMapper.existsBookmark(bookmarkDTO)).willReturn(1); // 북마크 있음
            given(bookmarkMapper.cancleBookmark(bookmarkDTO)).willReturn(1);

            // when
            boolean result = bookmarkService.toggleBookmark(bookmarkDTO);

            // then
            assertThat(result).isFalse(); // 북마크 해제됨
            then(bookmarkMapper).should().cancleBookmark(bookmarkDTO);
            then(bookmarkMapper).should(never()).insertBookmark(any());
        }

        @Test
        @DisplayName("실패: 로그인하지 않은 사용자")
        void toggleBookmark_NotLoggedIn() {
            // given
            bookmarkDTO.setMemberNo(0);

            // when & then
            assertThatThrownBy(() -> bookmarkService.toggleBookmark(bookmarkDTO))
                    .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("실패: 북마크 등록 실패")
        void toggleBookmark_InsertFail() {
            // given
            given(bookmarkMapper.existsBookmark(bookmarkDTO)).willReturn(0);
            given(bookmarkMapper.insertBookmark(bookmarkDTO)).willReturn(0);

            // when & then
            assertThatThrownBy(() -> bookmarkService.toggleBookmark(bookmarkDTO))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage("북마크 등록에 실패했습니다.");
        }

        @Test
        @DisplayName("실패: 북마크 해제 실패")
        void toggleBookmark_DeleteFail() {
            // given
            given(bookmarkMapper.existsBookmark(bookmarkDTO)).willReturn(1);
            given(bookmarkMapper.cancleBookmark(bookmarkDTO)).willReturn(0);

            // when & then
            assertThatThrownBy(() -> bookmarkService.toggleBookmark(bookmarkDTO))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage("북마크 해제에 실패했습니다.");
        }
    }
}
