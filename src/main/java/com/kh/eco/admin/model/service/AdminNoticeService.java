package com.kh.eco.admin.model.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.kh.eco.admin.model.dto.AdminBoardDetailDTO;
import com.kh.eco.admin.model.dto.NoticeRequestDTO;
import com.kh.eco.admin.model.dto.PageResponse;

public interface AdminNoticeService {
    PageResponse selectNoticeList(int pageNo);
    AdminBoardDetailDTO selectNoticeDetail(Long boardNo);
    void insertNotice(NoticeRequestDTO notice, List<MultipartFile> files);
    void updateNotice(NoticeRequestDTO notice, List<MultipartFile> files);
    void deleteNotice(Long boardNo);
}
