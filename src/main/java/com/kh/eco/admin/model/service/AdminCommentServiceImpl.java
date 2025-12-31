package com.kh.eco.admin.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.kh.eco.admin.model.dao.AdminCommentMapper;
import com.kh.eco.admin.model.dto.AdminCommentDTO;
import com.kh.eco.admin.model.dto.CommentPageResponse;
import com.kh.eco.exception.DeleteFailureException;
import com.kh.eco.exception.PageNotFoundException;
import com.kh.eco.exception.RestoreFailureException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

	private final AdminCommentMapper adminCommentMapper;
	
	@Override // 댓글 전체조회
	public CommentPageResponse findCommentAll(int pageNo) {
		RowBounds rb = new RowBounds(pageNo * 5, 5);
		List<AdminCommentDTO> commentList = adminCommentMapper.findCommentAll(rb);
		
		if(commentList == null) {
        	throw new PageNotFoundException("조회된 정보가 없습니다.");
        } else {
        	int totalCount = adminCommentMapper.countComments();
        	return new CommentPageResponse(commentList, totalCount);
        }
	}
	
    @Override // 신고된 댓글 조회
    public CommentPageResponse findReportedComment(int pageNo) {
        RowBounds rb = new RowBounds(pageNo * 5, 5);
        List<AdminCommentDTO> commentList = adminCommentMapper.findReportedComment(rb);
        
        if(commentList == null) {
        	throw new PageNotFoundException("조회된 정보가 없습니다.");
        } else {
        	int totalCount = adminCommentMapper.countReportedComments();
        	return new CommentPageResponse(commentList, totalCount);
        }
    }
    
    @Override // 댓글 상세조회
    public AdminCommentDTO findByCommentNo(Long commentNo) {
    	AdminCommentDTO comment = adminCommentMapper.selectComment(commentNo);
    	if(comment == null) {
    		throw new PageNotFoundException("조회된 정보가 없습니다.");
    	} else {
    		return comment;
    	}
    }

	@Override
	public void deleteComment(Long commentNo) {
		AdminCommentDTO comment = adminCommentMapper.selectComment(commentNo);
		
		try {
			if(comment == null) {
				throw new PageNotFoundException("존재하지 않는 댓글입니다.");
			} else if("N".equals(comment.getStatus())) {
				throw new DeleteFailureException("이미 삭제된 댓글입니다.");
			} else {
				adminCommentMapper.deleteComment(commentNo);
			}
		} catch(DeleteFailureException e) {
		    throw e; 
		} catch(RuntimeException e) {
		    throw new DeleteFailureException("댓글 삭제 실패");
		}
	}

	@Override
	public void restoreComment(Long commentNo) {
        AdminCommentDTO comment = adminCommentMapper.selectComment(commentNo);

        if (comment == null) {
            throw new PageNotFoundException("존재하지 않는 게시글입니다.");
        } else if ("Y".equals(comment.getStatus())) {
            throw new RestoreFailureException("이미 활성화된 게시글입니다.");
        }

        int result = adminCommentMapper.restoreComment(commentNo);

        if (result == 0) {
            throw new RestoreFailureException("게시글 복원 처리에 실패했습니다.");
        }

	}

	@Override
	public void handleReport(Long commentNo) {
		int selectC = adminCommentMapper.selectReport(commentNo);
		// log.info("오앵애애애애애앵{}", result);
		if(selectC < 1) {
			throw new PageNotFoundException("활성화된 신고가 없습니다.");
		}
		
		int result = adminCommentMapper.handleReport(commentNo);
		if(result < 1) {
			throw new DeleteFailureException("신고확인 처리중 문제가 발생했습니다.");
		}		
		
	}
    
}
