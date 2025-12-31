package com.kh.eco.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * API 에러 응답 포맷
 * 프론트엔드 에러 처리 일관성 제공
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private int status;                         // HTTP 상태 코드
    private String code;                        // 에러 코드 (예: AUTH_001, MEMBER_002)
    private String message;                     // 에러 메시지
    private String path;                        // 요청 경로
    private LocalDateTime timestamp;            // 에러 발생 시간
    private Map<String, String> details;        // 상세 에러 정보 (Validation 등)

    /**
     * 기본 에러 응답
     */
    public static ApiError of(int status, String code, String message, String path) {
        return ApiError.builder()
                .status(status)
                .code(code)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Validation 에러 응답 (필드별 에러 포함)
     */
    public static ApiError of(int status, String code, String message, String path, Map<String, String> details) {
        return ApiError.builder()
                .status(status)
                .code(code)
                .message(message)
                .path(path)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
