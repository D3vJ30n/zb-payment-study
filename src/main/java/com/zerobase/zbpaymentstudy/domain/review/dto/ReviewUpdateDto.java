package com.zerobase.zbpaymentstudy.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 리뷰 수정을 위한 데이터 전송 객체 (DTO)
 * 클라이언트로부터 리뷰 수정에 필요한 정보를 전달받는 record 클래스
 */
public record ReviewUpdateDto(
    /**
     * 수정할 매장 평점
     * 1점 이상 5점 이하의 정수값만 허용
     * null 값이 허용되지 않음
     */
    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다")
    Integer rating,

    /**
     * 수정할 리뷰 내용
     * 최대 1000자까지 작성 가능
     * 선택적 입력 가능 (null 허용)
     */
    @Size(max = 1000, message = "리뷰 내용은 1000자를 초과할 수 없습니다")
    String content
) {
}