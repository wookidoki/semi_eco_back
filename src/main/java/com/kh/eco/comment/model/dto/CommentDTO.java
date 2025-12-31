package com.kh.eco.comment.model.dto;

import java.sql.Timestamp;

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
public class CommentDTO {
	
	@Min(value = 1, message = "댓글 번호의 값이 1보다 작을 수 없습니다.")
	@NotNull(message = "NULL 값이 올 수 없습니다.")
	private Long commentNo;
	
	@Min(value = 1, message = "회원 번호의 값이 1보다 작을 수 없습니다.")
	private Integer refMno;
	
	@Min(value = 1, message = "게시글 번호의 값이 1보다 작을 수 없습니다.")
	private Long refBno;
	
	@NotBlank(message = "내용을 입력해주세요.")
	@NotNull(message = "NULL 값이 올 수 없습니다.")
	private String commentContent;
	
	private Timestamp regDate;
	private char status;
	private String memberName;
	private String boardTitle;
	
	// 댓글 조회용으로 추가
	private String memberId;      // 작성자 아이디
	private String memberImage;   // 작성자 프로필 이미지 경로

}
