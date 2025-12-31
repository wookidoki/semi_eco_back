package com.kh.eco.admin.model.dto;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class AdminMemberDTO {
	
	private int memberNo;
	
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영어/숫자만 사용 가능합니다.")
	@Size(min = 2, max = 20, message = "아이디 값은 2글자 이상 20글자 이하만 사용할 수 있습니다.")
	@NotBlank(message = "아이디는 필수 입력사항입니다.")
	private String memberId;
	
	private String memberName;
	private String phone;
	private String email;
	private int memberPoint;
	private String regionName;
	private String status;
	private String role;
	
}
