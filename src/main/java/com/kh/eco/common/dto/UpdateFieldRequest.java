package com.kh.eco.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 필드 업데이트 공통 요청 DTO
 * 회원 정보 수정 시 사용 (일반 사용자)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFieldRequest {

    @NotBlank(message = "필드명은 필수입니다.")
    private String field;          // "email", "phone", "password", "region"

    @NotBlank(message = "새 값은 필수입니다.")
    private String newValue;       // 새로운 값

    private String currentValue;   // 현재 값 (검증용, optional)

    /**
     * 필드 타입 Enum
     */
    public enum FieldType {
        EMAIL("email"),
        PHONE("phone"),
        PASSWORD("password"),
        REGION("region"),
        NAME("name"),
        PROFILE("profile");

        private final String value;

        FieldType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static FieldType from(String value) {
            for (FieldType type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("지원하지 않는 필드입니다: " + value);
        }
    }
}
