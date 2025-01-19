package com.zerobase.zbpaymentstudy.domain.store.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 매장 등록 요청 시 사용되는 데이터 전송 객체(DTO)
 * 매장 등록에 필요한 필수 정보들을 포함하는 불변 객체
 */
public record StoreRegisterDto(
    @NotBlank(message = "매장명은 필수입니다")
    String name,            // 등록할 매장의 이름

    @NotBlank(message = "매장 위치는 필수입니다")
    String location,        // 등록할 매장의 위치

    @NotBlank(message = "매장 설명은 필수입니다")
    String description      // 등록할 매장의 상세 설명
) {
    /**
     * 매장명을 반환하는 메서드
     *
     * @return 등록할 매장의 이름
     */
    public String getStoreName() {
        return this.name;
    }
}