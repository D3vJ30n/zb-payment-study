package com.zerobase.zbpaymentstudy.domain.reservation.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationCreateDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 예약 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 * 예약의 생성, 상태 변경, 체크인, 조회 등의 핵심 기능을 제공
 */
public interface ReservationService {
    /**
     * 새로운 예약을 생성하는 메서드
     * 회원이 매장에 대한 예약을 요청할 때 사용
     *
     * @param memberEmail 예약을 요청하는 회원의 이메일
     * @param dto         예약 생성에 필요한 정보를 담은 DTO
     * @return 생성된 예약 정보를 포함한 API 응답
     */
    ApiResponse<ReservationDto> createReservation(String memberEmail, ReservationCreateDto dto);

    /**
     * 예약 상태를 변경하는 메서드 (점장 전용)
     * 매장 점장이 예약을 승인하거나 거절할 때 사용
     *
     * @param reservationId 상태를 변경할 예약의 ID
     * @param status        변경하고자 하는 새로운 예약 상태
     * @return 변경된 예약 정보를 포함한 API 응답
     */
    ApiResponse<ReservationDto> updateReservationStatus(Long reservationId, ReservationStatus status);

    /**
     * 키오스크를 통한 체크인 처리 메서드
     * 고객이 매장 방문 시 키오스크에서 체크인할 때 사용
     *
     * @param reservationId    체크인할 예약의 ID
     * @param verificationCode 예약 확인을 위한 인증 코드
     * @return 체크인 처리된 예약 정보를 포함한 API 응답
     */
    ApiResponse<ReservationDto> checkIn(Long reservationId, String verificationCode);

    /**
     * 예약 목록을 조회하는 메서드
     * 다양한 검색 조건과 페이징을 지원
     *
     * @param criteria 예약 검색을 위한 조건
     * @param pageable 페이징 정보
     * @return 검색된 예약 목록과 페이징 정보
     */
    Page<ReservationDto> getReservations(ReservationSearchCriteria criteria, Pageable pageable);
}