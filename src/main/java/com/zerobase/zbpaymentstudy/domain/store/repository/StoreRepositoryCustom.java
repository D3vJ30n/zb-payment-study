package com.zerobase.zbpaymentstudy.domain.store.repository;

import com.zerobase.zbpaymentstudy.domain.store.dto.StoreSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {
    /**
     * 매장 검색 메서드
     *
     * @param criteria 검색 조건 (키워드, 위치 등)
     * @param pageable 페이징 정보
     * @return 검색된 매장 목록
     */
    Page<Store> searchStores(StoreSearchCriteria criteria, Pageable pageable);
}

