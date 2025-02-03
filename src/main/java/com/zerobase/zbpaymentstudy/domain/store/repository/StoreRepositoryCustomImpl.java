package com.zerobase.zbpaymentstudy.domain.store.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.store.entity.QStore;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 매장 검색을 위한 커스텀 리포지토리 구현 클래스
 * QueryDSL을 사용하여 동적 쿼리 기능을 구현
 */
@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QStore store = QStore.store;

    /**
     * 주어진 검색 조건에 따라 매장 목록을 조회하는 메서드
     * 페이징과 정렬을 지원하며, 동적 검색 조건을 적용
     *
     * @param criteria 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 검색된 매장 목록과 페이징 정보
     */
    @Override
    public Page<Store> searchStores(StoreSearchCriteria criteria, Pageable pageable) {
        JPAQuery<Store> query = queryFactory
            .selectFrom(store)
            .leftJoin(store.owner).fetchJoin()
            .where(
                keywordContains(criteria.keyword()),
                ownerEmailEquals(criteria.ownerEmail())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // 정렬 조건 적용
        for (OrderSpecifier<?> orderSpecifier : getOrderSpecifiers(pageable.getSort())) {
            query.orderBy(orderSpecifier);
        }

        List<Store> stores = query.fetch();
        long total = getCount(criteria);

        return new PageImpl<>(stores, pageable, total);
    }

    /**
     * 검색 조건에 맞는 전체 매장 수를 조회하는 메서드
     *
     * @param criteria 검색 조건 DTO
     * @return 조건에 맞는 매장의 총 개수
     */
    private long getCount(StoreSearchCriteria criteria) {
        return queryFactory
            .select(store.count())
            .from(store)
            .where(
                keywordContains(criteria.keyword()),
                ownerEmailEquals(criteria.ownerEmail())
            )
            .fetchOne();
    }

    /**
     * 매장명과 위치를 동시에 검색하는 조건을 생성하는 메서드
     *
     * @param keyword 검색할 키워드
     * @return 키워드 포함 여부를 확인하는 BooleanExpression
     */
    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? 
            store.name.containsIgnoreCase(keyword)
                .or(store.location.containsIgnoreCase(keyword)) : 
            null;
    }

    /**
     * 점주 이메일로 검색하는 조건을 생성하는 메서드
     *
     * @param email 검색할 점주 이메일
     * @return 이메일 일치 여부를 확인하는 BooleanExpression
     */
    private BooleanExpression ownerEmailEquals(String email) {
        return StringUtils.hasText(email) ? 
            store.owner.email.eq(email) : 
            null;
    }

    /**
     * 정렬 조건을 QueryDSL OrderSpecifier로 변환하는 메서드
     * 매장명, 위치, 생성일시에 대한 정렬을 지원
     *
     * @param sort Spring Data의 Sort 객체
     * @return QueryDSL OrderSpecifier 리스트
     */
    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            switch (order.getProperty()) {
                case "name" -> orders.add(order.isAscending() ?
                    store.name.asc() : store.name.desc());
                case "location" -> orders.add(order.isAscending() ?
                    store.location.asc() : store.location.desc());
                case "createdAt" -> orders.add(order.isAscending() ?
                    store.createdAt.asc() : store.createdAt.desc());
                default -> orders.add(order.isAscending() ?
                    store.id.asc() : store.id.desc());
            }
        });

        return orders;
    }
}