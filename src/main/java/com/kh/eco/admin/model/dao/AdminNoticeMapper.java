package com.kh.eco.admin.model.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import com.kh.eco.admin.model.dto.AdminBoardDTO;
import com.kh.eco.admin.model.dto.AttachmentDTO;
import com.kh.eco.admin.model.dto.NoticeRequestDTO;

@Mapper
public interface AdminNoticeMapper {
    List<AdminBoardDTO> selectNoticeList(RowBounds rowBounds);
    int countNotices();
    int insertNotice(NoticeRequestDTO notice);
    int insertAttachment(AttachmentDTO attachment);
    AdminBoardDTO selectNotice(Long boardNo);
    List<AttachmentDTO> selectAttachments(Long boardNo);
    int updateNotice(NoticeRequestDTO notice);
    int deleteNotice(Long boardNo);
    int deleteAttachments(Long boardNo);
}
