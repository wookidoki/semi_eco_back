package com.kh.eco.exception;

import com.kh.eco.common.constant.ErrorCode;

/**
 * 권한 없음 예외
 * 본인 게시글/댓글이 아닌 경우 등에 사용
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static UnauthorizedException notOwner() {
        return new UnauthorizedException("본인의 게시글/댓글만 수정/삭제할 수 있습니다.");
    }

    public static UnauthorizedException loginRequired() {
        return new UnauthorizedException(ErrorCode.UNAUTHORIZED, "로그인이 필요합니다.");
    }
}
