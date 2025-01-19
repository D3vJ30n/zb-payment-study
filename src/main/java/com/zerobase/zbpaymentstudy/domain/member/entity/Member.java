package com.zerobase.zbpaymentstudy.domain.member.entity;

import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원 정보를 저장하는 엔티티 클래스
 * 회원의 기본 정보와 인증/인가에 필요한 정보를 관리
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {
    /**
     * 회원 고유 식별자
     * 자동 증가하는 Primary Key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 회원 이메일 주소
     * 로그인 시 사용되는 고유한 식별자
     * null 불가, 중복 불가
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * 회원 비밀번호
     * 암호화되어 저장
     * null 불가
     */
    @Column(nullable = false)
    private String password;

    /**
     * 회원 이름
     * null 불가
     */
    @Column(nullable = false)
    private String name;

    /**
     * 회원 역할
     * USER: 일반 사용자
     * PARTNER: 매장 파트너(점장)
     * 문자열로 저장됨
     */
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    /**
     * 회원 생성 일시
     * 회원가입 시점의 시간이 자동으로 저장
     * null 불가
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 회원 정보 수정 일시
     * 정보 변경 시점의 시간이 자동으로 저장
     * null 불가
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}