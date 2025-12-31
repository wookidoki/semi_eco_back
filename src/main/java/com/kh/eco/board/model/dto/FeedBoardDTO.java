package com.kh.eco.board.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString
public class FeedBoardDTO {

	private Long boardNo;            // TB_BOARD 게시글번호
	private String boardTitle;       // TB_BOARD 게시글제목
	private String boardContent;     // TB_BOARD 게시글내용
	private String boardCategory;    // TB_BOARD 게시글카테고리 C%
	private String categoryName;     // TB_CATEGORY 카테고리이름 "인증"
	
	private Integer boardAuthor;        // TB_BOARD 게시글 작성자 회원번호
	private String memberId;         // TB_MEMBER 회원 아이디
	private String memberImage;      // TB_MEMBER 프로필 사진
	
	private String attachmentPath;   // TB_ATTACHMENT 첨부파일 (선택)
	

    private int regionNo;			 // TB_MEMBER 지역번호
	private String regionName;       // TB_REGION 지역이름  ("사용자 지역")
	
	private Date regDate;            // TB_BOARD 작성일자
	
	private Integer likeCount; // 좋아요 개수
	private Integer commentCount;
}
