package com.zerobase.zbpaymentstudy.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.util.StringUtils;

/**
 * 매장 등록 요청 시 사용되는 데이터 전송 객체(DTO)
 * 매장 등록에 필요한 필수 정보들을 포함하는 불변 객체
 */
public record StoreRegisterDto(
    @NotBlank(message = "매장명은 필수입니다")
    String name,            // 등록할 매장의 이름

    @NotBlank(message = "매장 위치는 필수입니다")
    String location,        // 등록할 매장의 위치

    @Size(max = 1000, message = "매장 설명은 1000자를 초과할 수 없습니다")
    String description,      // 등록할 매장의 상세 설명

    Double latitude,         // 추가
    Double longitude        // 추가
) {
    public StoreRegisterDto {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("매장 이름은 필수입니다.");
        }
        if (!StringUtils.hasText(location)) {
            throw new IllegalArgumentException("매장 위치는 필수입니다.");
        }
        if (latitude == null) {
            throw new IllegalArgumentException("위도는 필수입니다.");
        }
        if (longitude == null) {
            throw new IllegalArgumentException("경도는 필수입니다.");
        }
    }

    /**
     * 매장명을 반환하는 메서드
     *
     * @return 등록할 매장의 이름
     */
    public String getStoreName() {
        return this.name;
    }
}