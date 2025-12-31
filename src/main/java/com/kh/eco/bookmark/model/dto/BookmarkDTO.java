package com.kh.eco.bookmark.model.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
	
	@Min(value = 1, message = "회원 정보가 존재하지 않습니다.")
	private Integer memberNo;
	
	@Min(value = 1, message = "게시글 번호가 존재하지 않습니다.")
	private int boardNo;

}
