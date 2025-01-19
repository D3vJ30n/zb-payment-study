package com.zerobase.zbpaymentstudy.domain.member.dto;

import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * 회원가입 요청 데이터를 전달하기 위한 DTO
 * 회원가입 시 필요한 모든 필드에 대한 유효성 검증 규칙을 포함
 */
public record MemberSignUpDto(
    /*
     * 회원 이메일 주소
     * 로그인 시 사용될 고유한 식별자
     * 이메일 형식이어야 하며 필수 입력값
     */
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    String email,

    /*
     * 회원 비밀번호
     * 보안을 위해 최소 8자 이상이어야 하며 필수 입력값
     */
    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    String password,

    /*
     * 회원 이름
     * 필수 입력값
     */
    @NotBlank(message = "이름은 필수입니다")
    String name,

    /*
     * 회원 역할
     * USER: 일반 사용자
     * PARTNER: 매장 파트너(점장)
     * null 허용하지 않음
     */
    @NotNull(message = "회원 유형은 필수입니다")
    MemberRole role
) {
    /*
     * 레코드 컴팩트 생성자
     * 모든 필드의 null 체크 및 유효성 검증을 수행
     *
     * @throws IllegalArgumentException 필수 필드가 null이거나 빈 값인 경우
     */
    // 기본 검증은 어노테이션으로 처리되므로 추가 검증이 필요한 경우에만 여기에 구현
}