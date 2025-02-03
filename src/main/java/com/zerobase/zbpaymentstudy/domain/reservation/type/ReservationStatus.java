package com.zerobase.zbpaymentstudy.domain.reservation.type;

/**
 * 예약의 상태를 정의하는 열거형 클래스
 * 예약의 전체 생명주기에서 가능한 모든 상태값을 관리
 */
public enum ReservationStatus {
    /**
     * 예약 대기 상태
     * 고객이 예약을 요청한 직후의 초기 상태
     */
    PENDING("대기중"),

    /**
     * 승인된 상태
     * 매장 관리자가 예약 요청을 승인한 상태
     */
    APPROVED("승인됨"),

    /**
     * 거절된 상태
     * 매장 관리자가 예약 요청을 거절한 상태
     */
    REJECTED("거절됨"),

    /**
     * 체크인 완료 상태
     * 고객이 매장에 도착하여 체크인을 완료한 상태
     */
    CHECKED_IN("체크인"),

    /**
     * 이용 완료 상태
     * 예약된 서비스 이용이 완료된 상태
     */
    COMPLETED("완료"),

    /**
     * 취소된 상태
     * 고객이 예약을 취소하거나 노쇼(No-show)로 처리된 상태
     */
    CANCELLED("취소됨"),

    /**
     * 노쇼 상태
     * 고객이 예약된 서비스를 방문하지 않은 상태
     */
    NO_SHOW("노쇼");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}