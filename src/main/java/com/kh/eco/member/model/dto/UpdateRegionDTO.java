package com.kh.eco.member.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 지역 변경 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRegionDTO {

    private int currentRegion;

    @NotNull(message = "새 지역은 필수입니다.")
    @Positive(message = "올바른 지역 번호가 아닙니다.")
    private int newRegion;
}
