package com.zerobase.zbpaymentstudy.domain.reservation.dto;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 예약 정보를 전달하기 위한 데이터 전송 객체 (DTO)
 * 예약 엔티티의 정보를 클라이언트에게 전달하기 위한 record 클래스
 */
public record ReservationDto(
    /**
     * 예약의 고유 식별자
     */
    Long id,

    /**
     * 예약된 매장의 고유 식별자
     */
    Long storeId,

    /**
     * 예약된 매장의 이름
     */
    String storeName,

    /**
     * 예약한 회원의 이메일
     */
    String memberEmail,

    /**
     * 예약된 시간
     */
    LocalDateTime reservationTime,

    /**
     * 예약의 현재 상태
     * (예: PENDING, CONFIRMED, COMPLETED, CANCELLED 등)
     */
    ReservationStatus status,

    /**
     * 실제 체크인 시간
     * 고객이 매장에 도착하여 체크인한 시간을 기록
     */
    LocalDateTime checkInTime,

    /**
     * 예약이 생성된 시간
     */
    LocalDateTime createdAt
) {
    /**
     * Reservation 엔티티를 ReservationDto로 변환하는 정적 팩토리 메서드
     * 엔티티의 연관 관계에서 발생할 수 있는 null을 안전하게 처리
     *
     * @param reservation 변환할 Reservation 엔티티
     * @return 변환된 ReservationDto 객체
     */
    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
            reservation.getId(),
            Optional.ofNullable(reservation.getStore()).map(Store::getId).orElse(null),
            Optional.ofNullable(reservation.getStore()).map(Store::getName).orElse(null),
            Optional.ofNullable(reservation.getMember()).map(Member::getEmail).orElse(null),
            reservation.getReservationTime(),
            reservation.getStatus(),
            reservation.getCheckInTime(),
            reservation.getCreatedAt()
        );
    }
}