package com.zerobase.zbpaymentstudy.domain.store.dto;

import com.zerobase.zbpaymentstudy.domain.store.entity.Store;

import java.time.LocalDateTime;

/**
 * 매장 정보를 전달하기 위한 데이터 전송 객체(DTO)
 * Store 엔티티의 정보를 클라이언트에게 전달하기 위한 불변 객체
 */
public record StoreDto(
    Long id,                // 매장의 고유 식별자
    String name,            // 매장명
    String location,        // 매장 위치
    String description,     // 매장 설명
    String ownerEmail,      // 매장 소유자(점주)의 이메일
    LocalDateTime createdAt // 매장 등록 일시
) {
    /**
     * Store 엔티티를 StoreDto로 변환하는 생성자
     *
     * @param store 변환할 Store 엔티티 객체
     */
    public StoreDto(Store store) {
        this(
            store.getId(),
            store.getName(),
            store.getLocation(),
            store.getDescription(),
            store.getOwner().getEmail(),
            store.getCreatedAt()
        );
    }
} 