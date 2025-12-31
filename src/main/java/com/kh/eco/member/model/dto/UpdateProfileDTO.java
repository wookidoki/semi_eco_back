package com.kh.eco.member.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 프로필 이미지 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "newImage")
public class UpdateProfileDTO {

    private String memberId;
    private MultipartFile newImage;
    private String imagePath;
}
