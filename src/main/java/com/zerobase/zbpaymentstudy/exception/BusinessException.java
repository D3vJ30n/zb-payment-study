package com.zerobase.zbpaymentstudy.exception;

import lombok.Getter;

/**
 * 비즈니스 로직 처리 중 발생하는 예외를 처리하기 위한 커스텀 예외 클래스
 * ErrorCode 열거형과 함께 사용되어 일관된 예외 처리를 제공
 */
@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    /**
     * BusinessException 생성자
     *
     * @param errorCode 발생한 에러의 종류를 나타내는 ErrorCode
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * BusinessException 생성자
     *
     * @param errorCode 발생한 에러의 종류를 나타내는 ErrorCode
     * @param message 추가적인 에러 메시지
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * BusinessException 생성자
     *
     * @param errorCode 발생한 에러의 종류를 나타내는 ErrorCode
     * @param cause 원인이 되는 예외
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * BusinessException 생성자
     *
     * @param errorCode 발생한 에러의 종류를 나타내는 ErrorCode
     * @param message 추가적인 에러 메시지
     * @param cause 원인이 되는 예외
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
} 