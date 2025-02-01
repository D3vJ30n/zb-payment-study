package com.zerobase.zbpaymentstudy.controller;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.dto.MemberDto;
import com.zerobase.zbpaymentstudy.domain.member.dto.MemberSignUpDto;
import com.zerobase.zbpaymentstudy.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관련 API를 처리하는 컨트롤러
 * 회원가입, 로그인 등 인증 관련 엔드포인트를 관리
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    /**
     * 회원 서비스 의존성
     * 회원 관련 비즈니스 로직을 처리하는 서비스
     */
    private final MemberService memberService;

    /**
     * 회원가입 API 엔드포인트
     * 새로운 회원을 시스템에 등록
     *
     * @param signUpDto 회원가입 요청 데이터 (이메일, 비밀번호, 이름, 역할)
     * @return ResponseEntity<ApiResponse < MemberDto>> 회원가입 결과
     * - 성공: 200 OK와 함께 가입된 회원 정보 반환
     * - 실패: 400 Bad Request (잘못된 입력) 또는 500 Internal Server Error (서버 오류)
     * @throws IllegalArgumentException 이메일 중복 등 유효성 검증 실패 시
     */
    @PostMapping("sign-up")
    public ResponseEntity<ApiResponse<MemberDto>> signUp(
        @RequestBody @Valid MemberSignUpDto signUpDto
    ) {
        try {
            log.info("회원가입 요청 - email: {}", signUpDto.email());
            ApiResponse<MemberDto> response = memberService.signUp(signUpDto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패 - {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("ERROR", e.getMessage(), null));
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("ERROR", "서버 오류가 발생했습니다.", null));
        }
    }
}