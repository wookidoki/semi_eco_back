package com.kh.eco.admin.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.eco.admin.model.dao.AdminBoardMapper;
import com.kh.eco.admin.model.dto.AdminBoardDTO;
import com.kh.eco.admin.model.dto.AdminBoardDetailDTO;
import com.kh.eco.admin.model.dto.AttachmentDTO;
import com.kh.eco.admin.model.dto.PageResponse;
import com.kh.eco.exception.DeleteFailureException;
import com.kh.eco.exception.PageNotFoundException;
import com.kh.eco.exception.RestoreFailureException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminBoardServiceImpl implements AdminBoardService {
	
	private final AdminBoardMapper adminBoardMapper;
	
	
    @Override // 게시글 전체조회
    public PageResponse findBoardAll(int pageNo) {
        RowBounds rb = new RowBounds(pageNo * 5, 5);
        List<AdminBoardDTO> boardList = adminBoardMapper.findBoardAll(rb);
        
        if(boardList == null) {
        	throw new PageNotFoundException("조회된 정보가 없습니다.");
        } else {
        	int totalCount = adminBoardMapper.countBoards();
        	return new PageResponse(boardList, totalCount);
        }
    }
    
    @Override // 신고된 게시글 조회
    public PageResponse findReportedBoard(int pageNo) {
        RowBounds rb = new RowBounds(pageNo * 5, 5);
        List<AdminBoardDTO> boardList = adminBoardMapper.findReportedBoard(rb);
        
        if(boardList == null) {
        	throw new PageNotFoundException("조회된 정보가 없습니다.");
        } else {
        	int totalCount = adminBoardMapper.countReportedBoards();
        	return new PageResponse(boardList, totalCount);
        }
    }

    @Override // 게시글 상세조회
    public AdminBoardDetailDTO findByBoardNo(Long boardNo) {
    	AdminBoardDTO board = adminBoardMapper.selectBoard(boardNo);
    	if(board == null) {
    		throw new PageNotFoundException("조회된 정보가 없습니다.");
    	} else {
    		List<AttachmentDTO> attachment = adminBoardMapper.selectAttachemnt(boardNo);
    		return new AdminBoardDetailDTO(board, attachment);
    	}
    }
    
    @Override // 게시글 삭제
    public void deleteBoard(Long boardNo) {
        AdminBoardDTO board = adminBoardMapper.selectBoard(boardNo);

        if (board == null) {
            throw new PageNotFoundException("존재하지 않는 게시글입니다.");
        } else if ("N".equals(board.getStatus())) {
            throw new DeleteFailureException("이미 삭제된 게시글입니다.");
        }

        int result = adminBoardMapper.deleteBoard(boardNo);

        if (result == 0) {
            throw new DeleteFailureException("게시글 삭제 처리에 실패했습니다.");
        }
    }


	@Override // 게시글 복원
	public void restoreBoard(Long boardNo) {
        AdminBoardDTO board = adminBoardMapper.selectBoard(boardNo);

        log.info("board : {}" , board);
        if (board == null) {
            throw new PageNotFoundException("존재하지 않는 게시글입니다.");
        } else if ("Y".equals(board.getStatus())) {
            throw new RestoreFailureException("이미 활성화된 게시글입니다.");
        }

        int result = adminBoardMapper.restoreBoard(boardNo);

        if (result == 0) {
            throw new RestoreFailureException("게시글 복원 처리에 실패했습니다.");
        }

	}

	@Override // 게시글 신고 확인처리 (신고status변경)
	public void handleReport(Long boardNo) {
		int selectB = adminBoardMapper.selectReport(boardNo);
		// log.info("싫은데싫은데오앵애애애애애앵{}", result);
		if(selectB < 1) {
			throw new PageNotFoundException("활성화된 신고가 없습니다.");
		}
		
		int result = adminBoardMapper.handleReport(boardNo);
		if(result < 1) {
			throw new DeleteFailureException("신고확인 처리중 문제가 발생했습니다.");
		}
		
	}
	
}
