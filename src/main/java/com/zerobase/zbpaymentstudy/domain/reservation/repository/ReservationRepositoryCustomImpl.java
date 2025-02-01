package com.zerobase.zbpaymentstudy.domain.reservation.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.QReservation;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 예약 검색을 위한 커스텀 리포지토리 구현 클래스
 * QueryDSL을 사용하여 동적 쿼리 기능을 구현
 */
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QReservation reservation = QReservation.reservation;

    /**
     * 주어진 검색 조건에 따라 예약 목록을 조회
     * 페이징 처리와 동적 검색 조건을 지원
     *
     * @param criteria 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 검색된 예약 목록과 페이징 정보를 포함한 Page 객체
     */
    @Override
    public Page<Reservation> findReservations(ReservationSearchCriteria criteria, Pageable pageable) {
        List<Reservation> content = queryFactory
            .selectFrom(reservation)
            .where(
                memberEmailEquals(criteria.memberEmail()),
                storeIdEquals(criteria.storeId()),
                statusEquals(criteria.status()),
                betweenDates(criteria.startDate(), criteria.endDate())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = queryFactory
            .selectFrom(reservation)
            .where(
                memberEmailEquals(criteria.memberEmail()),
                storeIdEquals(criteria.storeId()),
                statusEquals(criteria.status()),
                betweenDates(criteria.startDate(), criteria.endDate())
            )
            .fetch()
            .size();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 회원 이메일로 검색하는 조건을 생성
     *
     * @param email 검색할 회원 이메일
     * @return 이메일 일치 여부를 확인하는 BooleanExpression
     */
    private BooleanExpression memberEmailEquals(String email) {
        return StringUtils.hasText(email) ? reservation.member.email.eq(email) : null;
    }

    /**
     * 매장 ID로 검색하는 조건을 생성
     *
     * @param storeId 검색할 매장 ID
     * @return 매장 ID 일치 여부를 확인하는 BooleanExpression
     */
    private BooleanExpression storeIdEquals(Long storeId) {
        return storeId != null ? reservation.store.id.eq(storeId) : null;
    }

    /**
     * 예약 상태로 검색하는 조건을 생성
     *
     * @param status 검색할 예약 상태
     * @return 예약 상태 일치 여부를 확인하는 BooleanExpression
     */
    private BooleanExpression statusEquals(ReservationStatus status) {
        return status != null ? reservation.status.eq(status) : null;
    }

    /**
     * 예약 시간 범위로 검색하는 조건을 생성
     * 시작 날짜와 종료 날짜가 모두 있는 경우 between 조건 사용
     * 둘 중 하나만 있는 경우 해당하는 조건만 적용
     *
     * @param startDate 검색 시작 날짜
     * @param endDate   검색 종료 날짜
     * @return 날짜 범위 조건을 확인하는 BooleanExpression
     */
    private BooleanExpression betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return reservation.reservationTime.between(startDate, endDate);
        }
        if (startDate != null) {
            return reservation.reservationTime.goe(startDate);
        }
        if (endDate != null) {
            return reservation.reservationTime.loe(endDate);
        }
        return null;
    }

    /**
     * 정렬 조건을 QueryDSL OrderSpecifier로 변환
     * 예약 시간, 생성 시간, ID에 대한 정렬을 지원
     *
     * @param sort Spring Data의 Sort 객체
     * @return QueryDSL OrderSpecifier 리스트
     */
    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            switch (order.getProperty()) {
                case "reservationTime" -> orders.add(order.isAscending() ?
                    reservation.reservationTime.asc() : reservation.reservationTime.desc());
                case "createdAt" -> orders.add(order.isAscending() ?
                    reservation.createdAt.asc() : reservation.createdAt.desc());
                default -> orders.add(order.isAscending() ?
                    reservation.id.asc() : reservation.id.desc());
            }
        });

        return orders;
    }
} 