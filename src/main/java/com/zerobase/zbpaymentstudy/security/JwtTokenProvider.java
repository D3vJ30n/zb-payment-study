package com.zerobase.zbpaymentstudy.security;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * JWT(JSON Web Token) 생성 및 검증을 담당하는 컴포넌트
 * 토큰의 생성, 파싱, 유효성 검증 등의 JWT 관련 기능을 제공
 */
@Slf4j
@Component
public class JwtTokenProvider {

    /**
     * JWT 토큰 제공자 생성자
     *
     * @param secretKey              JWT 서명에 사용될 비밀키 (application.yml에서 주입)
     * @param validityInMilliseconds 토큰의 유효기간 (밀리초 단위, application.yml에서 주입)
     */
    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration}") long validityInMilliseconds
    ) {
        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}