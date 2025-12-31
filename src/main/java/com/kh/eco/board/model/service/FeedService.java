package com.kh.eco.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.eco.auth.model.vo.CustomUserDetails;
import com.kh.eco.board.model.dto.BoardDTO;
import com.kh.eco.board.model.dto.FeedBoardDTO;

public interface FeedService {
	/**
     * 피드형 게시글 목록을 무한 스크롤 방식으로 조회합니다.
     *
     * @param category    게시판 카테고리 코드 (예: "C" - 챌린지/피드)
     * @param fetchOffset 이전에 조회한 마지막 게시글의 번호 (이 번호보다 작은 글을 조회, 첫 조회 시 null)
     * @param limit       한 번의 요청으로 가져올 게시글의 개수
     * @return 조건에 맞는 피드 게시글 목록 (FeedBoardDTO 리스트)
     */
	List<FeedBoardDTO> selectFeedList(String category, Long fetchOffset, Long limit);
	
	
	/**
	 * 댓글 작성을 위한 피드 조회 + 오버로딩
	 */
	List<FeedBoardDTO> selectFeedList(Long boarNo);
	
	
	 /**
     * 새로운 피드 게시글을 저장합니다.
     *
     * @param feed     저장할 피드 게시글 정보 (내용, 태그 등)
     * @param files    함께 업로드할 첨부 파일 (이미지 등)
     * @return 업로드된 파일 경로 리스트
     */
	List<String> insertFeed(FeedBoardDTO feed, List<MultipartFile> files);
	
	/**
	 * 피드를 삭제합니다.
	 * @param boardNo	게시글 번호
	 * @param userDetails	회원번호를 꺼내올 정보
	 * @return
	 */
	int deleteFeed(int boardNo, CustomUserDetails userDetails);
	
	/**
	 * 삭제하고자 하는 피드의 본인여부를 검증합니다.
	 * @param boardNo	게시글 번호
	 * @param userDetails	회원번호를 꺼내올 정보
	 * @return
	 */
	int isOwner(int boardNo, CustomUserDetails userDetails);
	
	/**
	 * 피드를 수정합니다.
	 * @param boardNo 게시글 번호
	 * @param feed 수정할 내용
	 * @param files 수정할 파일
	 * @param userDetails 회원번호를 꺼내올 정보
	 * @return 업로드된 파일 경로 리스트
	 */
	List<String> updateFeed(Long boardNo, FeedBoardDTO feed, List<MultipartFile> files, CustomUserDetails userDetails);
	
	List<String> findAttachments(Long boardNo);
	
	List<BoardDTO> selectPopularFeed(String category, Long fetchOffset, Long limit);
}
