package com.kh.eco.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

/**
 * 통합 API 응답 포맷
 * 모든 컨트롤러에서 일관된 응답 형식 제공
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {

    private int status;
    private String code;
    private String message;
    private T data;
    private String filePath;          // S3 파일 경로 (단일)
    private List<String> filePaths;   // S3 파일 경로 (다중)
    private LocalDateTime timestamp;

    // ==================== 성공 응답 (200 OK) ====================

    /**
     * 성공 응답 - 데이터만
     */
    public static <T> ResponseEntity<ResponseData<T>> ok(T data) {
        return ResponseEntity.ok(
                ResponseData.<T>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 성공 응답 - 메시지만 (Void 타입)
     */
    public static ResponseEntity<ResponseData<Void>> ok(String message) {
        return ResponseEntity.ok(
                ResponseData.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 성공 응답 - 데이터 + 메시지
     */
    public static <T> ResponseEntity<ResponseData<T>> ok(T data, String message) {
        return ResponseEntity.ok(
                ResponseData.<T>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ==================== 생성 응답 (201 Created) ====================

    /**
     * 생성 성공 응답 - 데이터 + 메시지
     */
    public static <T> ResponseEntity<ResponseData<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<T>builder()
                        .status(HttpStatus.CREATED.value())
                        .code("CREATED")
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 생성 성공 응답 - 데이터만
     */
    public static <T> ResponseEntity<ResponseData<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<T>builder()
                        .status(HttpStatus.CREATED.value())
                        .code("CREATED")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ==================== 기타 응답 ====================

    /**
     * 커스텀 상태 응답
     */
    public static <T> ResponseEntity<ResponseData<T>> of(HttpStatus httpStatus, String code, T data, String message) {
        return ResponseEntity.status(httpStatus).body(
                ResponseData.<T>builder()
                        .status(httpStatus.value())
                        .code(code)
                        .message(message)
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * No Content 응답 (204)
     */
    public static ResponseEntity<ResponseData<Void>> noContent() {
        return ResponseEntity.noContent().build();
    }

    // ==================== 파일 업로드 응답 ====================

    /**
     * 파일 업로드 성공 응답 - 파일 경로 + 메시지
     */
    public static ResponseEntity<ResponseData<Void>> okWithFile(String filePath, String message) {
        return ResponseEntity.ok(
                ResponseData.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .message(message)
                        .filePath(filePath)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 파일 업로드 성공 응답 - 데이터 + 파일 경로 + 메시지
     */
    public static <T> ResponseEntity<ResponseData<T>> okWithFile(T data, String filePath, String message) {
        return ResponseEntity.ok(
                ResponseData.<T>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .message(message)
                        .data(data)
                        .filePath(filePath)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 파일 생성 성공 응답 (201) - 파일 경로 + 메시지
     */
    public static ResponseEntity<ResponseData<Void>> createdWithFile(String filePath, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<Void>builder()
                        .status(HttpStatus.CREATED.value())
                        .code("CREATED")
                        .message(message)
                        .filePath(filePath)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 파일 생성 성공 응답 (201) - 데이터 + 파일 경로 + 메시지
     */
    public static <T> ResponseEntity<ResponseData<T>> createdWithFile(T data, String filePath, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<T>builder()
                        .status(HttpStatus.CREATED.value())
                        .code("CREATED")
                        .message(message)
                        .data(data)
                        .filePath(filePath)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ==================== 다중 파일 업로드 응답 ====================

    /**
     * 다중 파일 업로드 성공 응답 - 파일 경로 리스트 + 메시지
     */
    public static ResponseEntity<ResponseData<Void>> okWithFiles(List<String> filePaths, String message) {
        return ResponseEntity.ok(
                ResponseData.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .message(message)
                        .filePaths(filePaths)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 다중 파일 업로드 성공 응답 - 데이터 + 파일 경로 리스트 + 메시지
     */
    public static <T> ResponseEntity<ResponseData<T>> okWithFiles(T data, List<String> filePaths, String message) {
        return ResponseEntity.ok(
                ResponseData.<T>builder()
                        .status(HttpStatus.OK.value())
                        .code("SUCCESS")
                        .message(message)
                        .data(data)
                        .filePaths(filePaths)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 다중 파일 생성 성공 응답 (201) - 파일 경로 리스트 + 메시지
     */
    public static ResponseEntity<ResponseData<Void>> createdWithFiles(List<String> filePaths, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<Void>builder()
                        .status(HttpStatus.CREATED.value())
                        .code("CREATED")
                        .message(message)
                        .filePaths(filePaths)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * 다중 파일 생성 성공 응답 (201) - 데이터 + 파일 경로 리스트 + 메시지
     */
    public static <T> ResponseEntity<ResponseData<T>> createdWithFiles(T data, List<String> filePaths, String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.<T>builder()
                        .status(HttpStatus.CREATED.value())
                        .code("CREATED")
                        .message(message)
                        .data(data)
                        .filePaths(filePaths)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
