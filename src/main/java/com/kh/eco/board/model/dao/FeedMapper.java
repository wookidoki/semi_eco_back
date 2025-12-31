package com.kh.eco.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.FeedBoardDTO;
import com.kh.eco.board.model.vo.BoardVO;

@Mapper
public interface FeedMapper {
	/**
	 * 피드 게시글 조회 + 무한스크롤
	 * @param category
	 * @param fetchOffset
	 * @param limit
	 * @return
	 */
	List<FeedBoardDTO> selectFeedList(@Param("category")String category,
            @Param("fetchOffset")Long fetchOffset,
            @Param("limit")Long limit);
	
	/**
	 * 피드 게시글 작성 (수정중)
	 * @param feed
	 * @return
	 */
	int insertFeed(BoardVO feed);

	/**
	 * 피드 게시글 파일첨부 (수정중)
	 * @param boardNo
	 * @param filePath
	 */
	void saveAttachment(FeedBoardDTO boardDTO);
	
	/**
	 * 파일첨부 전 새로운 게시글 생성여부 검증 (수정중)
	 * @param memberNo
	 * @return
	 */
	BoardVO findNewBoardNo(int memberNo);
	
	/**
	 * 게시글 삭제하기
	 * @param boardNo	게시글 번호
	 * @return
	 */
	int deleteFeed(@Param("boardNo")int boardNo);
	
	/**
	 * 삭제할 게시글 본인여부 검증하기
	 * @param boardNo	게시글 번호
	 * @param memberNo	회원 번호
	 * @return
	 */
	int isOwner(@Param("boardNo")int boardNo, @Param("memberNo")int memberNo);
	
	int saveAttachment(Map<String, Object> fileMap);
	
	int updateFeed(FeedBoardDTO feed);
    
    int deleteAttachment(Long boardNo);
    
    List<String> selectAttachmentsByBoardNo(Long boardNo);
 
	List<BoardDTO> selectPopularFeed(@Param("category")String category,
            @Param("fetchOffset")Long fetchOffset,
            @Param("limit")Long limit);
	
}
