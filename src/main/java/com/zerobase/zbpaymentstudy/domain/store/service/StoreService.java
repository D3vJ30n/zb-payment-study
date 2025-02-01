package com.zerobase.zbpaymentstudy.domain.store.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreRegisterDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreSearchCriteria;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

/**
 * 매장 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 */
public interface StoreService {
    ApiResponse<StoreDto> registerStore(String ownerEmail, StoreRegisterDto registerDto);

    /**
     * 매장 검색 및 페이징 처리
     *
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬)
     * @param criteria 검색 조건 (매장명, 위치, 점주 이메일 등)
     * @return 페이징된 매장 정보 목록
     * @throws IllegalArgumentException 잘못된 검색 조건이 입력된 경우
     */
    Page<StoreDto> findStores(
        @NonNull Pageable pageable,
        @Valid @NonNull StoreSearchCriteria criteria
    );
}