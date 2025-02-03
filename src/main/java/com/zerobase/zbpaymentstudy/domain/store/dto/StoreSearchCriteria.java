package com.zerobase.zbpaymentstudy.domain.store.dto;

/**
 * 매장 검색 조건을 담는 DTO record
 */
public record StoreSearchCriteria(
    String keyword,           // 검색 키워드 (매장명 또는 위치)
    Double latitude,          // 위도
    Double longitude,         // 경도
    String ownerEmail,       // 점주 이메일
    String sortBy,           // 정렬 기준 (NAME, RATING, DISTANCE)
    String sortDirection     // 정렬 방향 (ASC, DESC)
) {
    // 정렬 기준 상수
    public static final String SORT_BY_NAME = "NAME";
    public static final String SORT_BY_RATING = "RATING";
    public static final String SORT_BY_DISTANCE = "DISTANCE";

    // 정렬 방향 상수
    public static final String SORT_ASC = "ASC";
    public static final String SORT_DESC = "DESC";

    /**
     * 검색 조건 유효성 검증을 위한 컴팩트 생성자
     */
    public StoreSearchCriteria {
        // sortBy가 지정된 경우 대문자로 변환
        if (sortBy != null) {
            sortBy = sortBy.toUpperCase();
            // 유효한 정렬 기준인지 검증
            if (!sortBy.equals(SORT_BY_NAME) && 
                !sortBy.equals(SORT_BY_RATING) && 
                !sortBy.equals(SORT_BY_DISTANCE)) {
                throw new IllegalArgumentException("Invalid sort criteria: " + sortBy);
            }
        }
        
        // sortDirection이 지정된 경우 대문자로 변환
        if (sortDirection != null) {
            sortDirection = sortDirection.toUpperCase();
            // 유효한 정렬 방향인지 검증
            if (!sortDirection.equals(SORT_ASC) && 
                !sortDirection.equals(SORT_DESC)) {
                throw new IllegalArgumentException("Invalid sort direction: " + sortDirection);
            }
        }
    }
}