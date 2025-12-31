package com.kh.eco.exception;

import com.kh.eco.common.constant.ErrorCode;

/**
 * 중복 데이터 예외
 * 아이디, 이메일, 전화번호 등의 중복 시 사용
 */
public class DuplicateException extends BusinessException {

    public DuplicateException(String message) {
        super(ErrorCode.DUPLICATE_DATA, message);
    }

    public DuplicateException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static DuplicateException memberId(String memberId) {
        return new DuplicateException(ErrorCode.DUPLICATE_MEMBER_ID,
            "이미 사용 중인 아이디입니다: " + memberId);
    }

    public static DuplicateException email(String email) {
        return new DuplicateException(ErrorCode.DUPLICATE_EMAIL,
            "이미 사용 중인 이메일입니다: " + email);
    }

    public static DuplicateException phone(String phone) {
        return new DuplicateException(ErrorCode.DUPLICATE_PHONE,
            "이미 사용 중인 전화번호입니다.");
    }
}
