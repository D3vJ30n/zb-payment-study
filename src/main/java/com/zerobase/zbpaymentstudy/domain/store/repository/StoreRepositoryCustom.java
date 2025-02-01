package com.zerobase.zbpaymentstudy.domain.store.repository;

import com.zerobase.zbpaymentstudy.domain.store.dto.StoreSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {
    /**
     * 주어진 검색 조건과 페이징 정보로 매장을 검색
     *
     * @param criteria 검색 조건
     * @param pageable 페이징 정보
     * @return 검색된 매장 목록
     */
    Page<Store> searchStores(StoreSearchCriteria criteria, Pageable pageable);
} 