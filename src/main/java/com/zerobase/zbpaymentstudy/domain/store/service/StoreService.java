package com.zerobase.zbpaymentstudy.domain.store.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreRegisterDto;

/**
 * 매장 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 */
public interface StoreService {
    /**
     * 새로운 매장을 등록하는 메서드
     *
     * @param ownerEmail  매장 소유자의 이메일
     * @param registerDto 매장 등록에 필요한 정보를 담은 DTO
     * @return ApiResponse<StoreDto> 매장 등록 결과 및 생성된 매장 정보
     */
    ApiResponse<StoreDto> registerStore(String ownerEmail, StoreRegisterDto registerDto);
}