package com.zerobase.zbpaymentstudy.domain.reservation.repository;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 예약 정보에 대한 데이터 접근을 담당하는 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공하며,
 * ReservationRepositoryCustom을 상속받아 커스텀 쿼리 기능을 확장
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    /**
     * 특정 회원의 특정 시간대 예약 존재 여부 확인
     *
     * @param member 예약 회원
     * @param start 시작 시간
     * @param end 종료 시간
     * @return 예약 존재 여부
     */
    boolean existsByMemberAndReservationTimeBetween(
        Member member,
        LocalDateTime start,
        LocalDateTime end
    );
    
    /**
     * 특정 매장의 특정 시간대 예약 수 조회
     *
     * @param store 매장
     * @param start 시작 시간
     * @param end 종료 시간
     * @return 예약 수
     */
    long countByStoreAndReservationTimeBetween(
        Store store,
        LocalDateTime start,
        LocalDateTime end
    );
}