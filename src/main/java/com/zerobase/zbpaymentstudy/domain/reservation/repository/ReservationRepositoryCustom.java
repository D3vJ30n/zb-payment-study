package com.zerobase.zbpaymentstudy.domain.reservation.repository;

import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 예약 조회를 위한 커스텀 리포지토리 인터페이스
 * QueryDSL을 사용한 동적 쿼리 기능을 정의
 */
public interface ReservationRepositoryCustom {
    /**
     * 주어진 검색 조건에 따라 예약 목록을 조회
     *
     * @param criteria 검색 조건
     * @param pageable 페이징 정보
     * @return 검색된 예약 목록
     */
    Page<Reservation> findReservations(ReservationSearchCriteria criteria, Pageable pageable);
}