package com.kh.eco.board.model.service;

import java.util.List;

import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.BoardReportDTO;

public interface BoardReportService {

	int boardReport(int boardNo, BoardReportDTO reportDTO);
	
	BoardDTO selectBoardOne(int boardNo);
	
	
}
