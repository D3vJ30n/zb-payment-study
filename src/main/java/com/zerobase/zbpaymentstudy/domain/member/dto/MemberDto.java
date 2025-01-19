package com.zerobase.zbpaymentstudy.domain.member.dto;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;

import java.time.LocalDateTime;

/**
 * 회원 정보를 전달하기 위한 데이터 전송 객체(DTO)
 * Entity와 API 계층 사이의 데이터 전달을 담당
 * 비밀번호와 같은 민감한 정보는 제외하고 필요한 정보만 포함
 */
public record MemberDto(
    /*
     * 회원 고유 식별자
     */
    Long id,

    /*
     * 회원 이메일 주소
     * 로그인 시 사용되는 고유한 식별자
     */
    String email,

    /*
     * 회원 이름
     */
    String name,

    /*
     * 회원 역할
     * USER: 일반 사용자
     * PARTNER: 매장 파트너(점장)
     */
    MemberRole role,

    /*
     * 회원 가입 일시
     */
    LocalDateTime createdAt
) {
    /**
     * Member 엔티티를 MemberDto로 변환하는 생성자
     * 엔티티의 필요한 정보만 선택적으로 DTO로 변환
     *
     * @param member 변환할 Member 엔티티
     */
    public MemberDto(Member member) {
        this(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getRole(),
            member.getCreatedAt()
        );
    }
}