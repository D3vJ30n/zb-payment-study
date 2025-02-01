package com.zerobase.zbpaymentstudy.domain.store.dto;

import com.zerobase.zbpaymentstudy.domain.store.entity.Store;

import java.time.LocalDateTime;

/**
 * 매장 정보를 전달하기 위한 데이터 전송 객체(DTO)
 * Store 엔티티의 정보를 클라이언트에게 전달하기 위한 불변 객체
 */
public record StoreDto(
    /**
     * 매장의 고유 식별자
     * 데이터베이스에서 자동 생성되는 ID 값
     */
    Long id,

    /**
     * 매장명
     * 매장을 식별하기 위한 이름
     */
    String name,

    /**
     * 매장 위치
     * 매장의 실제 물리적 주소 또는 위치 정보
     */
    String location,

    /**
     * 매장 설명
     * 매장에 대한 상세 설명이나 소개 정보
     */
    String description,

    /**
     * 매장 소유자(점주)의 이메일
     * 매장을 운영하는 점주의 식별 정보
     */
    String ownerEmail,

    /**
     * 매장 등록 일시
     * 매장이 시스템에 등록된 날짜와 시간
     */
    LocalDateTime createdAt
) {
    /**
     * Store 엔티티를 StoreDto로 변환하는 정적 팩토리 메서드
     * 엔티티의 정보를 클라이언트에게 전달하기 위한 DTO로 매핑
     *
     * @param store 변환할 Store 엔티티
     * @return 변환된 StoreDto 객체
     */
    public static StoreDto from(Store store) {
        return new StoreDto(
            store.getId(),
            store.getName(),
            store.getLocation(),
            store.getDescription(),
            store.getOwner().getEmail(),
            store.getCreatedAt()
        );
    }
}