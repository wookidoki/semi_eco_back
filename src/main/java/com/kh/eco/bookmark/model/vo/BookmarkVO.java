package com.kh.eco.bookmark.model.vo;

import java.sql.Date;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkVO {
	
	@Min(value = 1, message = "회원 정보가 존재하지 않습니다.")
	private Long refMno;
	
	@Min(value = 1, message = "게시글 번호가 존재하지 않습니다.")
	private Long refBno;
	
	private Date regDate;
	private char status;
	
}
