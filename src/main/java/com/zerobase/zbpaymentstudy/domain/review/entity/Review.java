package com.zerobase.zbpaymentstudy.domain.review.entity;

import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 리뷰 정보를 관리하는 엔티티 클래스
 * 매장에 대한 고객의 리뷰 정보를 저장하고 관리
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review {
    /**
     * 리뷰의 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 리뷰가 작성된 예약 정보
     * 지연 로딩을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    /**
     * 매장에 대한 평점 (1-5점)
     * null 값을 허용하지 않음
     */
    @Column(nullable = false)
    private Integer rating;

    /**
     * 리뷰 내용
     * 최대 1000자까지 저장 가능
     */
    @Column(length = 1000)
    private String content;

    /**
     * 리뷰 생성 시간
     */
    private LocalDateTime createdAt;

    /**
     * 리뷰 수정 시간
     */
    private LocalDateTime updatedAt;
}