package com.zerobase.zbpaymentstudy.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 캐시 설정을 관리하는 설정 클래스
 * <p>
 * 캐시 관련 Bean 설정 및 캐시 전략을 정의
 * - CacheManager 설정
 * - 캐시 만료 시간 설정
 * - 캐시 키 생성 전략
 * - 캐시 저장소 설정 (예: 메모리, Redis 등)
 *
 * @Configuration 어노테이션 필요
 * @EnableCaching 어노테이션으로 캐시 기능 활성화
 */
@Configuration
@EnableCaching
/*
  캐시 설정을 관리하는 클래스
  애플리케이션의 성능 향상을 위한 캐시 전략과 설정을 정의
 */
public class CacheConfig {

    /**
     * 엔티티 매니저 객체
     * JPA 영속성 컨텍스트를 관리하기 위해 사용
     *
     * @return 설정이 완료된 CacheManager 객체
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("stores"),
            new ConcurrentMapCache("members")
        ));
        return cacheManager;
    }
} 