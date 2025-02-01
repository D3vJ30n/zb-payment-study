package com.zerobase.zbpaymentstudy.domain.review.repository;

import com.zerobase.zbpaymentstudy.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 리뷰 정보에 대한 데이터 접근을 담당하는 리포지토리 인터페이스
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * 특정 예약에 대한 리뷰 존재 여부 확인
     *
     * @param reservationId 확인할 예약 ID
     * @return 리뷰 존재 여부
     */
    boolean existsByReservationId(Long reservationId);

    /**
     * 특정 매장의 모든 리뷰를 조회
     * N+1 문제를 방지하기 위해 연관 엔티티를 함께 조회
     *
     * @param storeId  조회할 매장 ID
     * @param pageable 페이징 정보
     * @return 매장의 리뷰 목록
     */
    @Query("SELECT r FROM Review r " +
        "JOIN FETCH r.reservation res " +
        "JOIN FETCH res.member " +
        "JOIN FETCH res.store s " +
        "WHERE s.id = :storeId")
    Page<Review> findByStoreId(Long storeId, Pageable pageable);
}