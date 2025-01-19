package com.zerobase.zbpaymentstudy.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/*
 * 전역 예외 처리를 담당하는 핸들러 클래스
 * 애플리케이션에서 발생하는 모든 예외를 일관된 형식으로 처리
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<?>> createErrorResponse(
        String message,
        HttpStatus status,
        Exception e,
        boolean isError
    ) {
        if (isError) {
            log.error(message, e);
        } else {
            log.warn(message, e.getMessage());
        }
        return ResponseEntity.status(status)
            .body(new ApiResponse<>("ERROR", message, null));
    }

    /**
     * IllegalArgumentException 예외 처리
     * 주로 잘못된 입력값이나 비즈니스 로직 검증 실패 시 발생하는 예외를 처리
     *
     * @param e 발생한 IllegalArgumentException 예외
     * @return 400 Bad Request 상태코드와 에러 메시지를 포함한 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(
        IllegalArgumentException e
    ) {
        return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, e, false);
    }

    /*
     * MethodArgumentNotValidException 예외 처리
     *
     * @param e 발생한 MethodArgumentNotValidException 예외
     * @return 400 Bad Request 상태코드와 검증 실패 상세 내용을 포함한 응답
     * @Valid 어노테이션을 통한 요청 데이터 검증 실패 시 발생하는 예외를 처리
     * 각 필드별 유효성 검증 실패 내용을 상세히 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(
        MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format(
                "[%s](은)는 %s 입력된 값: [%s]",
                error.getField(),
                error.getDefaultMessage(),
                error.getRejectedValue()
            ))
            .collect(Collectors.joining(", "));

        return createErrorResponse(message, HttpStatus.BAD_REQUEST, e, false);
    }

    /*
     * RuntimeException 예외 처리
     * 예상치 못한 런타임 예외를 처리
     *
     * @param e 발생한 RuntimeException 예외
     * @return 500 Internal Server Error 상태코드와 일반적인 에러 메시지를 포함한 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {
        return createErrorResponse("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e, true);
    }

    /*
     * 기타 모든 예외 처리
     * 위에서 처리되지 않은 모든 예외를 처리하는 마지막 단계의 핸들러
     *
     * @param e 발생한 Exception
     * @return 500 Internal Server Error 상태코드와 일반적인 에러 메시지를 포함한 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return createErrorResponse("예기치 않은 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, e, true);
    }
}