package com.zerobase.zbpaymentstudy.domain.member.type;

/**
 * 회원의 역할을 정의하는 열거형 클래스
 * 시스템 내에서 사용자의 권한과 접근 수준을 구분하는데 사용
 */
public enum MemberRole {
    USER,      // 일반 사용자
    PARTNER    // 매장 파트너(점장)
} 