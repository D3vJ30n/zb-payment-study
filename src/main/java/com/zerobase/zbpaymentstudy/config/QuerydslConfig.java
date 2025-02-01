package com.zerobase.zbpaymentstudy.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Querydsl 설정을 관리하는 설정 클래스
 * <p>
 * Querydsl 관련 Bean 설정 및 기본 설정을 정의
 * - JPAQueryFactory Bean 등록
 * - EntityManager 설정
 * - Querydsl 관련 옵션 설정
 * <p>
 * 동적 쿼리 생성과 타입 안전성을 보장하는 Querydsl 사용을 위한 기본 설정 제공
 *
 * @Configuration 어노테이션 필요
 * @PersistenceContext로 EntityManager 주입
 */
@Configuration
public class QuerydslConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
} 