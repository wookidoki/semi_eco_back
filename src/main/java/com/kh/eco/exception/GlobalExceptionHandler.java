package com.kh.eco.exception;

import com.kh.eco.common.constant.ErrorCode;
import com.kh.eco.common.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 모든 예외를 일관된 형식의 ApiError로 반환
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== 신규 통합 예외 핸들러 ====================

    /**
     * BusinessException 통합 핸들러
     * 모든 비즈니스 예외의 기본 핸들러
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException e,
            HttpServletRequest request
    ) {
        log.warn("비즈니스 예외: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ApiError error = ApiError.of(
                errorCode.getStatus(),
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(error);
    }

    /**
     * ResourceNotFoundException 핸들러 (404)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException e,
            HttpServletRequest request
    ) {
        log.warn("리소스 미발견: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ApiError error = ApiError.of(
                errorCode.getStatus(),
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * DuplicateException 핸들러 (409)
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiError> handleDuplicateException(
            DuplicateException e,
            HttpServletRequest request
    ) {
        log.warn("중복 데이터: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ApiError error = ApiError.of(
                errorCode.getStatus(),
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    /**
     * InvalidRequestException 핸들러 (400)
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequestException(
            InvalidRequestException e,
            HttpServletRequest request
    ) {
        log.warn("잘못된 요청: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ApiError error = ApiError.of(
                errorCode.getStatus(),
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * UnauthorizedException 핸들러 (403)
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(
            UnauthorizedException e,
            HttpServletRequest request
    ) {
        log.warn("권한 없음: {}", e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        ApiError error = ApiError.of(
                errorCode.getStatus(),
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(error);
    }

    /**
     * 신규 UsernameNotFoundException 핸들러 (404)
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleNewUsernameNotFoundException(
            UsernameNotFoundException e,
            HttpServletRequest request
    ) {
        log.warn("사용자 미발견: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.MEMBER_NOT_FOUND.getStatus(),
                ErrorCode.MEMBER_NOT_FOUND.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    // ==================== 기존 예외 핸들러 (하위 호환성) ====================

    /**
     * 인증 실패 예외 처리 (401)
     */
    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<ApiError> handleCustomAuthenticationException(
            CustomAuthenticationException e,
            HttpServletRequest request
    ) {
        log.warn("인증 실패: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.AUTHENTICATION_FAILED.getStatus(),
                ErrorCode.AUTHENTICATION_FAILED.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    /**
     * 기존 UsenameNotFoundException 핸들러 (404) - 하위 호환성
     */
    @ExceptionHandler(UsenameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(
            UsenameNotFoundException e,
            HttpServletRequest request
    ) {
        log.warn("사용자 미발견: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.MEMBER_NOT_FOUND.getStatus(),
                ErrorCode.MEMBER_NOT_FOUND.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * 아이디 중복 예외 처리 (409)
     */
    @ExceptionHandler(IdDuplicateException.class)
    public ResponseEntity<ApiError> handleIdDuplicateException(
            IdDuplicateException e,
            HttpServletRequest request
    ) {
        log.warn("아이디 중복: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.DUPLICATE_MEMBER_ID.getStatus(),
                ErrorCode.DUPLICATE_MEMBER_ID.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    /**
     * 회원정보 중복 예외 처리 (409)
     */
    @ExceptionHandler(MemberInfoDuplicatedException.class)
    public ResponseEntity<ApiError> handleMemberInfoDuplicatedException(
            MemberInfoDuplicatedException e,
            HttpServletRequest request
    ) {
        log.warn("회원정보 중복: {}", e.getMessage());

        ErrorCode errorCode = e.getMessage().contains("이메일")
                ? ErrorCode.DUPLICATE_EMAIL
                : ErrorCode.DUPLICATE_PHONE;

        ApiError error = ApiError.of(
                errorCode.getStatus(),
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    /**
     * Validation 예외 처리 (400)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        log.warn("Validation 에러: {}", e.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ApiError error = ApiError.of(
                ErrorCode.INVALID_INPUT.getStatus(),
                ErrorCode.INVALID_INPUT.getCode(),
                "입력값 검증에 실패했습니다.",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * 잘못된 파라미터 예외 처리 (400)
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ApiError> handleInvalidParameterException(
            InvalidParameterException e,
            HttpServletRequest request
    ) {
        log.warn("잘못된 파라미터: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.INVALID_INPUT.getStatus(),
                ErrorCode.INVALID_INPUT.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * Java 표준 IllegalArgumentException 처리 (400)
     */
    @ExceptionHandler(java.lang.IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            java.lang.IllegalArgumentException e,
            HttpServletRequest request
    ) {
        log.warn("잘못된 인자: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.INVALID_INPUT.getStatus(),
                ErrorCode.INVALID_INPUT.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * 로그아웃 실패 예외 처리 (400)
     */
    @ExceptionHandler(LogoutFailureException.class)
    public ResponseEntity<ApiError> handleLogoutFailureException(
            LogoutFailureException e,
            HttpServletRequest request
    ) {
        log.warn("로그아웃 실패: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.LOGOUT_FAILED.getStatus(),
                ErrorCode.LOGOUT_FAILED.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * 페이지 미발견 예외 처리 (404)
     */
    @ExceptionHandler(PageNotFoundException.class)
    public ResponseEntity<ApiError> handlePageNotFoundException(
            PageNotFoundException e,
            HttpServletRequest request
    ) {
        log.warn("페이지 미발견: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.PAGE_NOT_FOUND.getStatus(),
                ErrorCode.PAGE_NOT_FOUND.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * 삭제 실패 예외 처리 (400)
     */
    @ExceptionHandler(DeleteFailureException.class)
    public ResponseEntity<ApiError> handleDeleteFailureException(
            DeleteFailureException e,
            HttpServletRequest request
    ) {
        log.warn("삭제 실패: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.BOARD_DELETE_FAILED.getStatus(),
                ErrorCode.BOARD_DELETE_FAILED.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * 복구 실패 예외 처리 (400)
     */
    @ExceptionHandler(RestoreFailureException.class)
    public ResponseEntity<ApiError> handleRestoreFailureException(
            RestoreFailureException e,
            HttpServletRequest request
    ) {
        log.warn("복구 실패: {}", e.getMessage());

        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "DATA_004",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    /**
     * 조회 실패 예외 처리 (404)
     */
    @ExceptionHandler(FindFailureException.class)
    public ResponseEntity<ApiError> handleFindFailureException(
            FindFailureException e,
            HttpServletRequest request
    ) {
        log.warn("조회 실패: {}", e.getMessage());

        ApiError error = ApiError.of(
                ErrorCode.DATA_NOT_FOUND.getStatus(),
                ErrorCode.DATA_NOT_FOUND.getCode(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * SQL 응답 예외 처리 (500)
     */
    @ExceptionHandler(SQLResponseException.class)
    public ResponseEntity<ApiError> handleSQLResponseException(
            SQLResponseException e,
            HttpServletRequest request
    ) {
        log.error("SQL 예외: {}", e.getMessage(), e);

        ApiError error = ApiError.of(
                ErrorCode.SQL_ERROR.getStatus(),
                ErrorCode.SQL_ERROR.getCode(),
                "데이터베이스 처리 중 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    /**
     * SQL 예외 처리 (500)
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleSQLException(
            SQLException e,
            HttpServletRequest request
    ) {
        log.error("SQL 예외: {}", e.getMessage(), e);

        ApiError error = ApiError.of(
                ErrorCode.DATABASE_ERROR.getStatus(),
                ErrorCode.DATABASE_ERROR.getCode(),
                "데이터베이스 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    /**
     * 데이터 액세스 예외 처리 (500)
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(
            DataAccessException e,
            HttpServletRequest request
    ) {
        log.error("데이터 액세스 예외: {}", e.getMessage(), e);

        ApiError error = ApiError.of(
                ErrorCode.DATABASE_ERROR.getStatus(),
                ErrorCode.DATABASE_ERROR.getCode(),
                "데이터베이스 접근 중 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    /**
     * RuntimeException 처리 (500)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(
            RuntimeException e,
            HttpServletRequest request
    ) {
        log.error("런타임 예외: {}", e.getMessage(), e);

        ApiError error = ApiError.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage() != null ? e.getMessage() : "서버 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    /**
     * 기타 예외 처리 (500)
     * 모든 예외의 최종 방어선
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("예상치 못한 예외 발생: {}", e.getMessage(), e);

        ApiError error = ApiError.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                "서버 내부 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
