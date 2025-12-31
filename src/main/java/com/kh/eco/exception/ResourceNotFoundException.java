package com.kh.eco.exception;

import com.kh.eco.common.constant.ErrorCode;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외
 * 게시글, 댓글, 회원 등을 찾지 못했을 때 사용
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(ErrorCode.DATA_NOT_FOUND, message);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static ResourceNotFoundException board(Long boardNo) {
        return new ResourceNotFoundException(ErrorCode.BOARD_NOT_FOUND,
            "게시글을 찾을 수 없습니다. boardNo: " + boardNo);
    }

    public static ResourceNotFoundException comment(Long commentNo) {
        return new ResourceNotFoundException(ErrorCode.COMMENT_NOT_FOUND,
            "댓글을 찾을 수 없습니다. commentNo: " + commentNo);
    }

    public static ResourceNotFoundException member(String memberId) {
        return new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND,
            "회원을 찾을 수 없습니다. memberId: " + memberId);
    }
}
