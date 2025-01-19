package com.zerobase.zbpaymentstudy.domain.member.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.dto.MemberDto;
import com.zerobase.zbpaymentstudy.domain.member.dto.MemberSignUpDto;

/**
 * 회원 관련 비즈니스 로직을 처리하는 서비스 인터페이스
 * 회원가입, 회원정보 수정 등의 회원 관련 핵심 비즈니스 로직을 정의
 */
public interface MemberService {

    /**
     * 새로운 회원을 등록하는 회원가입 메서드
     *
     * @param signUpDto 회원가입에 필요한 회원 정보를 담은 DTO
     * @return ApiResponse<MemberDto> 회원가입 결과 및 생성된 회원 정보를 담은 응답 객체
     * - 성공 시: 생성된 회원 정보를 MemberDto로 반환
     * - 실패 시: 적절한 에러 메시지와 함께 실패 응답 반환
     */
    ApiResponse<MemberDto> signUp(MemberSignUpDto signUpDto);
} 