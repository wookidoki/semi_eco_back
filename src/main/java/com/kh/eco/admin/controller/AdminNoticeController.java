package com.kh.eco.admin.controller;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.http.ResponseEntity;
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

import com.kh.eco.admin.model.dao.AdminNoticeMapper;
import com.kh.eco.admin.model.dto.AdminBoardDTO;
import com.kh.eco.admin.model.dto.AdminBoardDetailDTO;
import com.kh.eco.admin.model.dto.NoticeRequestDTO;
import com.kh.eco.admin.model.dto.PageResponse;
import com.kh.eco.admin.model.service.AdminNoticeService;
import com.kh.eco.common.dto.ResponseData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;
    private final AdminNoticeMapper adminNoticeMapper;

    /**
     * 공지사항 목록 조회
     */
    @GetMapping
    public ResponseEntity<ResponseData<PageResponse>> getNoticeList(
            @RequestParam(name = "page", defaultValue = "0") int pageNo) {

        PageResponse result = adminNoticeService.selectNoticeList(pageNo);
        return ResponseData.ok(result);
    }

    /**
     * 공지사항 상세 조회
     */
    @GetMapping("/{boardNo}")
    public ResponseEntity<ResponseData<AdminBoardDetailDTO>> getNoticeDetail(
            @PathVariable("boardNo") Long boardNo) {

        AdminBoardDetailDTO detail = adminNoticeService.selectNoticeDetail(boardNo);
        return ResponseData.ok(detail);
    }

    /**
     * 공지사항 등록 (관리자 전용)
     */
    @PostMapping
    public ResponseEntity<ResponseData<Void>> registNotice(
            @RequestPart("notice") NoticeRequestDTO notice,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        if (notice.getRefMno() == null) {
            notice.setRefMno(1L);
        }

        adminNoticeService.insertNotice(notice, files);
        return ResponseData.created(null, "공지사항이 등록되었습니다.");
    }

    /**
     * 공지사항 수정
     */
    @PutMapping("/{boardNo}")
    public ResponseEntity<ResponseData<Void>> modifyNotice(
            @PathVariable("boardNo") Long boardNo,
            @RequestPart("notice") NoticeRequestDTO notice,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        notice.setBoardNo(boardNo);
        adminNoticeService.updateNotice(notice, files);
        return ResponseData.ok("공지사항이 수정되었습니다.");
    }

    /**
     * 공지사항 삭제
     */
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<ResponseData<Void>> removeNotice(
            @PathVariable("boardNo") Long boardNo) {

        adminNoticeService.deleteNotice(boardNo);
        return ResponseData.ok("공지사항이 삭제되었습니다.");
    }


    /**
     * 목록 테스트
     */
    @GetMapping("/test-list")
    public ResponseEntity<ResponseData<List<AdminBoardDTO>>> testList() {
        RowBounds rb = new RowBounds(0, 10);
        List<AdminBoardDTO> list = adminNoticeMapper.selectNoticeList(rb);
        return ResponseData.ok(list);
    }
}
