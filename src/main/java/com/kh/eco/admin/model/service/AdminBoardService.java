package com.kh.eco.admin.model.service;

import com.kh.eco.admin.model.dto.AdminBoardDetailDTO;
import com.kh.eco.admin.model.dto.PageResponse;

public interface AdminBoardService {
	
	PageResponse findBoardAll(int pageNo);
	
	PageResponse findReportedBoard(int pageNo);
	
	AdminBoardDetailDTO findByBoardNo(Long boardNo);
	
	void deleteBoard(Long boardNo);
	
	void restoreBoard(Long boardNo);
	
	void handleReport(Long boardNo);

}
