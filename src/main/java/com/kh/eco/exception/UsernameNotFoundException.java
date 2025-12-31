package com.kh.eco.exception;

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String message) {
        super(message);
    }

    public UsernameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
