package com.zerobase.zbpaymentstudy.domain.store.dto;

/**
 * 매장 검색 조건을 담는 클래스
 * 매장 목록 조회 시 필요한 검색 조건들을 포함
 */
public record StoreSearchCriteria(
    String name,        // 매장명으로 검색
    String location,    // 위치로 검색
    String ownerEmail   // 점장 이메일로 검색
) {
}