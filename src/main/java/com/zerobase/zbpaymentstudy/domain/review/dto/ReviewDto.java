package com.zerobase.zbpaymentstudy.domain.review.dto;

import com.zerobase.zbpaymentstudy.domain.review.entity.Review;

import java.time.LocalDateTime;

/**
 * 리뷰 정보를 전달하기 위한 데이터 전송 객체 (DTO)
 * 리뷰 엔티티의 정보를 클라이언트에게 전달하기 위한 record 클래스
 */
public record ReviewDto(
    /**
     * 리뷰의 고유 식별자
     */
    Long id,

    /**
     * 리뷰가 작성된 예약의 고유 식별자
     */
    Long reservationId,

    /**
     * 리뷰를 작성한 회원의 이메일
     */
    String memberEmail,

    /**
     * 리뷰가 작성된 매장의 이름
     */
    String storeName,

    /**
     * 매장에 대한 평점 (1-5점)
     */
    Integer rating,

    /**
     * 리뷰 내용
     */
    String content,

    /**
     * 리뷰가 작성된 시간
     */
    LocalDateTime createdAt
) {
    /**
     * Review 엔티티를 ReviewDto로 변환하는 정적 팩토리 메서드
     * 엔티티의 연관 관계에서 필요한 정보를 추출하여 DTO로 변환
     *
     * @param review 변환할 Review 엔티티
     * @return 변환된 ReviewDto 객체
     */
    public static ReviewDto from(Review review) {
        return new ReviewDto(
            review.getId(),
            review.getReservation().getId(),
            review.getReservation().getMember().getEmail(),
            review.getReservation().getStore().getName(),
            review.getRating(),
            review.getContent(),
            review.getCreatedAt()
        );
    }
}