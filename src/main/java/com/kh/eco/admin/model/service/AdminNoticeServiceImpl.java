package com.kh.eco.admin.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.admin.model.dao.AdminNoticeMapper;
import com.kh.eco.admin.model.dto.AdminBoardDTO;
import com.kh.eco.admin.model.dto.AdminBoardDetailDTO;
import com.kh.eco.admin.model.dto.AttachmentDTO;
import com.kh.eco.admin.model.dto.NoticeRequestDTO;
import com.kh.eco.admin.model.dto.PageResponse;
import com.kh.eco.exception.DeleteFailureException;
import com.kh.eco.exception.PageNotFoundException;
import com.kh.eco.file.FileUploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminNoticeServiceImpl implements AdminNoticeService {

    private final AdminNoticeMapper adminNoticeMapper;
    private final FileUploadService fileUploadService;

    public AdminNoticeServiceImpl(
            AdminNoticeMapper adminNoticeMapper,
            @Qualifier("s3FileUploadService") FileUploadService fileUploadService) {
        this.adminNoticeMapper = adminNoticeMapper;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public PageResponse selectNoticeList(int pageNo) {
        // 기존 코드와 동일한 RowBounds 방식 사용
        RowBounds rb = new RowBounds(pageNo * 5, 5); // 한 페이지당 5개
        List<AdminBoardDTO> list = adminNoticeMapper.selectNoticeList(rb);
        
        if (list == null) {
            throw new PageNotFoundException("조회된 공지사항이 없습니다.");
        }
        
        int totalCount = adminNoticeMapper.countNotices();
        return new PageResponse(list, totalCount);
    }

    @Override
    public AdminBoardDetailDTO selectNoticeDetail(Long boardNo) {
        AdminBoardDTO board = adminNoticeMapper.selectNotice(boardNo);
        if (board == null) {
            throw new PageNotFoundException("존재하지 않는 공지사항입니다.");
        }
        
        List<AttachmentDTO> attachments = adminNoticeMapper.selectAttachments(boardNo);
        return new AdminBoardDetailDTO(board, attachments);
    }

    @Override
    @Transactional
    public void insertNotice(NoticeRequestDTO notice, List<MultipartFile> files) {
        // 01. 게시글(공지사항) 등록
        int result = adminNoticeMapper.insertNotice(notice);
        if (result == 0) throw new RuntimeException("공지사항 등록 실패");

        // 02. 파일 저장 및 Attachment 등록
        if (files != null && !files.isEmpty()) {
            saveFiles(files, notice.getBoardNo());
        }
    }

    @Override
    @Transactional
    public void updateNotice(NoticeRequestDTO notice, List<MultipartFile> files) {
        // 01. 게시글 내용 수정
        int result = adminNoticeMapper.updateNotice(notice);
        if (result == 0) throw new RuntimeException("공지사항 수정 실패");

        // 02. 파일 처리 (간단하게 기존 파일 유지 + 새 파일 추가 방식으로 구현)
        // 만약 기존 파일을 삭제하고 싶다면 deleteAttachments 호출 필요
        if (files != null && !files.isEmpty()) {
            saveFiles(files, notice.getBoardNo());
        }
    }

    @Override
    @Transactional
    public void deleteNotice(Long boardNo) {
        int result = adminNoticeMapper.deleteNotice(boardNo);
        if (result == 0) {
            throw new DeleteFailureException("공지사항 삭제 실패 (이미 삭제되었거나 존재하지 않음)");
        }
        // 첨부파일도 Soft Delete
        adminNoticeMapper.deleteAttachments(boardNo);
    }

    /**
     * 파일 저장 및 DB 등록
     * FileUploadService를 통해 파일을 저장하고 첨부파일 정보를 DB에 등록
     */
    private void saveFiles(List<MultipartFile> files, Long boardNo) {
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();

            // FileUploadService를 통해 파일 업로드
            String fileUrl = fileUploadService.upload(file, "notice");

            // DB Insert용 DTO 생성
            AttachmentDTO attachment = new AttachmentDTO();
            attachment.setRefBno(boardNo);
            attachment.setOriginalFileName(originalName);
            attachment.setModifiedFileName(extractFileName(fileUrl));
            attachment.setAttachmentPath(fileUrl);

            adminNoticeMapper.insertAttachment(attachment);
        }
    }

    /**
     * URL에서 파일명 추출
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null || !fileUrl.contains("/")) {
            return fileUrl;
        }
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}
