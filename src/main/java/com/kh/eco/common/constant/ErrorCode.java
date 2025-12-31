package com.kh.eco.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의
 * 프론트엔드에서 에러 타입별 처리 가능
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 인증 관련 (AUTH_xxx)
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_003", "만료된 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_004", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_005", "접근 권한이 없습니다."),
    LOGOUT_FAILED(HttpStatus.BAD_REQUEST, "AUTH_006", "로그아웃에 실패했습니다."),

    // 회원 관련 (MEMBER_xxx)
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_001", "회원을 찾을 수 없습니다."),
    DUPLICATE_MEMBER_ID(HttpStatus.CONFLICT, "MEMBER_002", "이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "MEMBER_003", "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "MEMBER_004", "이미 사용 중인 전화번호입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER_005", "비밀번호가 일치하지 않습니다."),
    MEMBER_SUSPENDED(HttpStatus.FORBIDDEN, "MEMBER_006", "정지된 회원입니다."),

    // 게시글 관련 (BOARD_xxx)
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_001", "게시글을 찾을 수 없습니다."),
    BOARD_DELETE_FAILED(HttpStatus.BAD_REQUEST, "BOARD_002", "게시글 삭제에 실패했습니다."),
    BOARD_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "BOARD_003", "게시글 수정에 실패했습니다."),
    BOARD_CREATE_FAILED(HttpStatus.BAD_REQUEST, "BOARD_004", "게시글 작성에 실패했습니다."),
    UNAUTHORIZED_BOARD_ACCESS(HttpStatus.FORBIDDEN, "BOARD_005", "게시글 접근 권한이 없습니다."),

    // 댓글 관련 (COMMENT_xxx)
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_001", "댓글을 찾을 수 없습니다."),
    COMMENT_DELETE_FAILED(HttpStatus.BAD_REQUEST, "COMMENT_002", "댓글 삭제에 실패했습니다."),
    COMMENT_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "COMMENT_003", "댓글 수정에 실패했습니다."),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "COMMENT_004", "댓글 접근 권한이 없습니다."),

    // 파일 관련 (FILE_xxx)
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "FILE_001", "파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "FILE_002", "파일 삭제에 실패했습니다."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "FILE_003", "지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE_004", "파일 크기가 제한을 초과했습니다."),

    // 데이터 관련 (DATA_xxx)
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "DATA_001", "데이터를 찾을 수 없습니다."),
    DUPLICATE_DATA(HttpStatus.CONFLICT, "DATA_002", "중복된 데이터입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "DATA_003", "잘못된 입력값입니다."),

    // 페이징 관련 (PAGE_xxx)
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "PAGE_001", "페이지를 찾을 수 없습니다."),
    INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, "PAGE_002", "잘못된 페이지 요청입니다."),

    // 서버 관련 (SERVER_xxx)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_001", "서버 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_002", "데이터베이스 오류가 발생했습니다."),
    SQL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_003", "SQL 실행 중 오류가 발생했습니다."),

    // 북마크/좋아요 관련 (ACTION_xxx)
    BOOKMARK_FAILED(HttpStatus.BAD_REQUEST, "ACTION_001", "북마크 처리에 실패했습니다."),
    LIKE_FAILED(HttpStatus.BAD_REQUEST, "ACTION_002", "좋아요 처리에 실패했습니다."),

    // 기타
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_001", "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON_002", "지원하지 않는 HTTP 메서드입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public int getStatus() {
        return httpStatus.value();
    }
}
