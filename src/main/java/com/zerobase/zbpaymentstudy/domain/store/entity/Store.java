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
@Table(name = "store", indexes = {
    @Index(name = "idx_store_name", columnList = "name"),
    @Index(name = "idx_store_location", columnList = "location"),
    @Index(name = "idx_store_owner", columnList = "owner_id")
})
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 매장의 고유 식별자

    @Column(nullable = false)
    private String name;            // 매장명

    @Column(nullable = false)
    private String location;        // 매장 위치

    @Column(length = 1000)
    private String description;     // 매장 설명

    @Column(nullable = false)
    private Double latitude;  // 위도

    @Column(nullable = false)
    private Double longitude; // 경도

    @Column(nullable = false)
    @Builder.Default
    private Double averageRating = 0.0;  // 평균 평점

    @Column(nullable = false)
    @Builder.Default
    private Integer reviewCount = 0;      // 리뷰 수

    /**
     * 매장 소유자(점주) 정보
     * 지연 로딩(LAZY)을 사용하여 필요할 때만 소유자 정보를 로드
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Member owner;           // 매장 소유자(Member 엔티티 참조)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 매장 등록 일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 매장 정보 수정 일시
}