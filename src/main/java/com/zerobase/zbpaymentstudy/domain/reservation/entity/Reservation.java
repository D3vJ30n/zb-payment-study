package com.zerobase.zbpaymentstudy.domain.reservation.entity;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 예약 정보를 관리하는 엔티티 클래스
 * 매장 예약에 대한 모든 정보를 저장하고 관리
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation {
    /**
     * 예약의 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 예약된 매장 정보
     * 지연 로딩을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    /**
     * 예약한 회원 정보
     * 지연 로딩을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 예약 일시
     * null을 허용하지 않음
     */
    @Column(nullable = false)
    private LocalDateTime reservationTime;

    /**
     * 예약 상태
     * PENDING, APPROVED, REJECTED, CHECKED_IN, COMPLETED, CANCELLED
     */
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    /**
     * 실제 체크인 시간
     */
    private LocalDateTime checkInTime;

    /**
     * 예약 생성 시간
     */
    private LocalDateTime createdAt;

    /**
     * 예약 정보 수정 시간
     */
    private LocalDateTime updatedAt;
}