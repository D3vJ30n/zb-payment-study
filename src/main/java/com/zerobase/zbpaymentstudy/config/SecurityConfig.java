package com.zerobase.zbpaymentstudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정을 담당하는 설정 클래스
 * 인증/인가 관련 전반적인 보안 설정을 관리
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Spring Security Filter Chain 설정
     * 보안 관련 필터 체인을 구성하고 URL별 접근 권한을 설정
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // CSRF 보호 기능 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            // 세션 관리 설정 - JWT 사용을 위해 STATELESS로 설정
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // URL별 접근 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 인증 관련 엔드포인트는 모든 사용자 접근 허용
                .requestMatchers("/api/auth/**").permitAll()
                // 매장 관련 엔드포인트는 PARTNER 역할을 가진 사용자만 접근 가능
                .requestMatchers("/api/stores/**").hasRole("PARTNER")
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated())
            .build();
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     * BCrypt 해시 함수를 사용하여 비밀번호를 안전하게 암호화
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}