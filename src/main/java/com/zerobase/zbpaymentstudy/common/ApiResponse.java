package com.zerobase.zbpaymentstudy.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 응답의 표준 포맷을 정의하는 클래스
 * 모든 API 응답은 이 클래스의 형식을 따름
 *
 * @param <T> 응답 데이터의 타입
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * API 처리 결과 상태
     * "SUCCESS": 정상 처리
     * "ERROR": 오류 발생
     */
    private String result;

    /**
     * API 처리 결과 메시지
     * 성공 시: 성공 메시지
     * 실패 시: 오류 메시지
     */
    private String message;

    /**
     * API 응답 데이터
     * 성공 시: 요청에 대한 처리 결과 데이터
     * 실패 시: null
     */
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("ERROR", message, data);
    }
}