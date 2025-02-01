package com.zerobase.zbpaymentstudy.domain.reservation.repository;

import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 예약 정보에 대한 데이터 접근을 담당하는 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공하며,
 * ReservationRepositoryCustom을 상속받아 커스텀 쿼리 기능을 확장
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
}