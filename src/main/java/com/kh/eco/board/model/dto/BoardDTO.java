package com.kh.eco.board.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDTO {
	
	private Long boardNo;      
	
	private String memberName;   
	
	@NotBlank(message = "카테고리를 선택해주세요.")
	private String boardCategory;   

	@NotBlank(message = "제목을 입력해주세요.")
	private String boardTitle;      
	
	@NotBlank(message = "내용을 입력해주세요.")
	private String boardContent;   
	
	private Date regDate;         
	private int viewCount;        
	private int likeCount;        
	
	private String boardWriter;
	
	private String attachmentPath;
}