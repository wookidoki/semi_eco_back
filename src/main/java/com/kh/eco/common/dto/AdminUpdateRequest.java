package com.kh.eco.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관리자용 회원 정보 수정 통합 DTO
 * 17개의 Update*ByAdminDTO를 하나로 통합
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateRequest {

    @NotNull(message = "회원 번호는 필수입니다.")
    @Positive(message = "회원 번호는 양수여야 합니다.")
    private Integer memberNo;      // 수정할 회원 번호

    @NotBlank(message = "필드명은 필수입니다.")
    private String field;          // "id", "password", "email", "phone", "region", "name", "point", "status", "role"

    @NotBlank(message = "새 값은 필수입니다.")
    private String newValue;       // 새로운 값

    private Integer regionNo;      // region 수정 시 필요 (optional)

    /**
     * 관리자 수정 가능 필드 Enum
     */
    public enum AdminFieldType {
        ID("id"),
        PASSWORD("password"),
        EMAIL("email"),
        PHONE("phone"),
        REGION("region"),
        NAME("name"),
        POINT("point"),
        STATUS("status"),
        ROLE("role");

        private final String value;

        AdminFieldType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AdminFieldType from(String value) {
            for (AdminFieldType type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("지원하지 않는 필드입니다: " + value);
        }
    }
}
