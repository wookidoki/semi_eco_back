package com.kh.eco.comment.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommentReportDTO {
	@Min(value = 1, message = "잘못된 접근입니다.")
	private Long commentReportNo;
	
	private int refMno;
	
	@Min(value = 1, message = "잘못된 접근입니다.")
	private Long refCno;
	
	@NotNull(message = "카테고리 선택을 필수입니다.")
	@Min(value = 1, message = "카테고리 번호는 1 이상이어야 합니다.")
	@Max(value = 5, message = "카테고리 번호는 5 이하여야 합니다.")
	private int refRcno;
	
	@NotBlank(message = "신고 사유를 입력해주세요.")
	private String reportContent;
	
	private Date regDate;
	private char status;
}
