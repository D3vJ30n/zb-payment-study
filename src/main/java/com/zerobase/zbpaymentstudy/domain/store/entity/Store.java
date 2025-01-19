package com.zerobase.zbpaymentstudy.domain.store.entity;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 매장 정보를 저장하는 엔티티 클래스
 * 매장의 기본 정보와 소유자 정보를 관리
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 매장의 고유 식별자

    @Column(nullable = false)
    private String name;            // 매장명

    @Column(nullable = false)
    private String location;        // 매장 위치

    @Column(nullable = false)
    private String description;     // 매장 설명

    /**
     * 매장 소유자(점주) 정보
     * 지연 로딩(LAZY)을 사용하여 필요할 때만 소유자 정보를 로드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;           // 매장 소유자(Member 엔티티 참조)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 매장 등록 일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 매장 정보 수정 일시
}