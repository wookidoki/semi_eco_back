package com.kh.eco.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.eco.board.model.dao.BoardMapper;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.BoardReportDTO;
import com.kh.eco.exception.PageNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardReportServiceImpl implements BoardReportService {
	
	private final BoardService boardService;
	private final BoardMapper boardMapper;
	
	/**
	 * 게시글 신고
	 */
	@Override
	public int boardReport(int boardNo, BoardReportDTO reportDTO) {
		// 게시글 존재 여부
		selectBoardOne(boardNo);
		/*if(boardService.selectBoardDetail(boardNo) == null) {
			throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
		}*/
		return boardMapper.boardReport(reportDTO);
		
		
	}
	
	/**
	 * 신고된 게시글 번호 존재여부 확인용
	 */
	@Override
	public BoardDTO selectBoardOne(int boardNo) {
		BoardDTO b = boardMapper.selectBoardOne(boardNo);
		log.info("{}", b);
		if(b == null) {
			throw new PageNotFoundException("게시글이 존재하지 않습니다.");
		} return b;
	}
	
	

}
