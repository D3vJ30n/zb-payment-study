package com.zerobase.zbpaymentstudy.controller;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreRegisterDto;
import com.zerobase.zbpaymentstudy.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 매장 관련 API를 처리하는 컨트롤러
 * 매장 등록, 조회 등 매장 관리 관련 엔드포인트를 관리
 * PARTNER 권한을 가진 사용자만 접근 가능
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    /**
     * 매장 서비스 의존성
     * 매장 관련 비즈니스 로직을 처리하는 서비스
     */
    private final StoreService storeService;

    /**
     * 매장 등록 API 엔드포인트
     * PARTNER 권한을 가진 회원이 새로운 매장을 등록
     *
     * @param email       JWT 토큰에서 추출한 인증된 사용자의 이메일
     * @param registerDto 매장 등록 요청 데이터 (매장명, 위치, 설명)
     * @return ResponseEntity<ApiResponse < StoreDto>> 매장 등록 결과
     * - 성공: 200 OK와 함께 등록된 매장 정보 반환
     * - 실패: 400 Bad Request (잘못된 입력 또는 권한 없음) 또는 500 Internal Server Error (서버 오류)
     * @throws IllegalArgumentException 유효하지 않은 사용자 또는 PARTNER 권한이 없는 경우
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StoreDto>> registerStore(
        @AuthenticationPrincipal String email,
        @RequestBody @Valid StoreRegisterDto registerDto
    ) {
        try {
            log.info("매장 등록 요청 - email: {}, storeName: {}", email, registerDto.getStoreName());
            ApiResponse<StoreDto> response = storeService.registerStore(email, registerDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("매장 등록 실패 - {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            log.error("매장 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("ERROR", "서버 오류가 발생했습니다.", null));
        }
    }
}