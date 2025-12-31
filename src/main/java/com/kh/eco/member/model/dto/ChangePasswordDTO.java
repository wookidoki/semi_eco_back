package com.kh.eco.member.model.dto;

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
public class ChangePasswordDTO {
	
	private String currentPassword;
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,20}$",
			message = "비밀번호는 8~20자이며, 대문자/소문자/숫자/특수문자를 각각 1개 이상 포함해야 합니다.")
	@NotBlank(message = "비밀번호는 필수 입력사항입니다.")
	private String newPassword;
}
