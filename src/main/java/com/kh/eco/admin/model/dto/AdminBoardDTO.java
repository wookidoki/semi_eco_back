package com.kh.eco.admin.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminBoardDTO {
	
	// [기존 필드] - 신고 관리용 (유지)
	private Long boardNo;
	private String boardTitle;
	private String boardContent;
	private Date regDate;
	private String status;
	private int boardReportCount;
	private String memberId;

	// [추가된 필드] - 공지사항 관리용
	private String categoryName;
	private String boardCategory;
	private int viewCount;
	private String memberName;

}