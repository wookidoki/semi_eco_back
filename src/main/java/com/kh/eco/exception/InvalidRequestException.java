package com.kh.eco.exception;

import com.kh.eco.common.constant.ErrorCode;

/**
 * 잘못된 요청에 대한 예외
 * java.lang.IllegalArgumentException 대신 사용
 */
public class InvalidRequestException extends BusinessException {

    public InvalidRequestException(String message) {
        super(ErrorCode.INVALID_INPUT, message);
    }

    public InvalidRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
