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
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QStore store = QStore.store;

    @Override
    public Page<Store> searchStores(StoreSearchCriteria criteria, Pageable pageable) {
        JPAQuery<Store> query = queryFactory
            .selectFrom(store)
            .distinct();

        query.leftJoin(store.owner).fetchJoin();

        BooleanExpression whereCondition = createWhereCondition(criteria);
        if (whereCondition != null) {
            query.where(whereCondition);
        }

        applySorting(query, criteria);

        List<Store> stores = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(store.count())
            .from(store)
            .where(whereCondition);

        return PageableExecutionUtils.getPage(stores, pageable, countQuery::fetchOne);
    }

    private BooleanExpression createWhereCondition(StoreSearchCriteria criteria) {
        BooleanExpression condition = null;

        if (StringUtils.hasText(criteria.keyword())) {
            condition = store.name.containsIgnoreCase(criteria.keyword())
                .or(store.location.containsIgnoreCase(criteria.keyword()));
        }

        if (StringUtils.hasText(criteria.ownerEmail())) {
            BooleanExpression ownerCondition = store.owner.email.eq(criteria.ownerEmail());
            condition = condition == null ? ownerCondition : condition.and(ownerCondition);
        }

        return condition;
    }

    private void applySorting(JPAQuery<Store> query, StoreSearchCriteria criteria) {
        String sortBy = criteria.sortBy();
        String direction = criteria.sortDirection();

        if (!StringUtils.hasText(sortBy)) {
            query.orderBy(store.name.asc());
            return;
        }

        switch (sortBy.toUpperCase()) {
            case "NAME" -> query.orderBy(
                "DESC".equalsIgnoreCase(direction) ? 
                    store.name.desc() : store.name.asc()
            );
            case "RATING" -> {
                if ("DESC".equalsIgnoreCase(direction)) {
                    query.orderBy(store.averageRating.desc(), store.reviewCount.desc());
                } else {
                    query.orderBy(store.averageRating.asc(), store.reviewCount.desc());
                }
            }
            default -> query.orderBy(store.name.asc());
        }
    }
} 