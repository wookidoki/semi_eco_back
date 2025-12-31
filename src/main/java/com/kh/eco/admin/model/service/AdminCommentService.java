package com.kh.eco.admin.model.service;

import com.kh.eco.admin.model.dto.AdminCommentDTO;
import com.kh.eco.admin.model.dto.CommentPageResponse;

public interface AdminCommentService {
	
	CommentPageResponse findCommentAll(int pageNo);
	
	CommentPageResponse findReportedComment(int pageNo);
	
	AdminCommentDTO findByCommentNo(Long commentNo);
	
	void deleteComment(Long commentNo);
	
	void restoreComment(Long commentNo);
	
	void handleReport(Long commentNo);

}
