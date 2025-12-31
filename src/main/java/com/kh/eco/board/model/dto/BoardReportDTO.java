package com.kh.eco.board.model.dto;

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
public class BoardReportDTO {
	@Min(value = 1, message = "잘못된 접근입니다.")
	private Long boardReportNo;
	
	@Min(value = 1, message = "잘못된 접근입니다.")
	private Long refBno;
	
	@NotNull(message = "카테고리 선택은 필수입니다.")
	@Min(value = 1, message = "잘못된 접근입니다.")
	@Max(value = 5, message = "잘못된 접근입니다.")
	private Long refRcno;
	
	@NotBlank(message = "내용을 입력해주세요.")
	private String reportContent;
	
	private Date regDate;
	private char status;
	
}
