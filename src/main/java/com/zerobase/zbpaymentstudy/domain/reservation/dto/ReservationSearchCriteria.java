package com.zerobase.zbpaymentstudy.domain.reservation.dto;

import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;

import java.time.LocalDateTime;

/**
 * 예약 검색을 위한 검색 조건 데이터 전송 객체 (DTO)
 * 예약 목록 조회 시 사용되는 다양한 검색 조건들을 포함하는 record 클래스
 */
public record ReservationSearchCriteria(
    /**
     * 회원 이메일 검색 조건
     * 특정 회원의 예약만을 조회하고자 할 때 사용
     */
    String memberEmail,

    /**
     * 매장 ID 검색 조건
     * 특정 매장의 예약만을 조회하고자 할 때 사용
     */
    Long storeId,

    /**
     * 예약 상태 검색 조건
     * 특정 상태(PENDING, CONFIRMED, COMPLETED, CANCELLED 등)의 예약만을 조회하고자 할 때 사용
     */
    ReservationStatus status,

    /**
     * 검색 시작 날짜
     * 이 날짜 이후의 예약만을 조회하고자 할 때 사용
     */
    LocalDateTime startDate,

    /**
     * 검색 종료 날짜
     * 이 날짜 이전의 예약만을 조회하고자 할 때 사용
     */
    LocalDateTime endDate
) {
}