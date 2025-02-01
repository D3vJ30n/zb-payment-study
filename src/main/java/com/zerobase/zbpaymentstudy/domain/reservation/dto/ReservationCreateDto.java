package com.zerobase.zbpaymentstudy.domain.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 예약 생성을 위한 데이터 전송 객체 (DTO)
 * 클라이언트로부터 예약 생성에 필요한 정보를 전달받는 record 클래스
 */
public record ReservationCreateDto(
    /**
     * 예약하고자 하는 매장의 고유 식별자
     * null 값이 허용되지 않으며, 유효한 매장 ID가 필수
     */
    @NotNull(message = "매장 ID는 필수입니다")
    Long storeId,

    /**
     * 예약 희망 일시
     * null 값이 허용되지 않으며, 현재 시간 이후의 시간만 지정 가능
     * @Future 어노테이션을 통해 미래 시간만 입력 가능하도록 제한
     */
    @NotNull(message = "예약 시간은 필수입니다")
    @Future(message = "예약 시간은 현재 시간 이후여야 합니다")
    LocalDateTime reservationTime
) {
}