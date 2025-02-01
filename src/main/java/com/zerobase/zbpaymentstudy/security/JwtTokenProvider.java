package com.zerobase.zbpaymentstudy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT(JSON Web Token) 생성 및 검증을 담당하는 컴포넌트
 * 토큰의 생성, 파싱, 유효성 검증 등의 JWT 관련 기능을 제공
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long validityInMilliseconds;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * JWT 토큰 제공자 생성자
     *
     * @param secretKey              JWT 서명에 사용될 비밀키 (application.yml에서 주입)
     * @param validityInMilliseconds 토큰의 유효기간 (밀리초 단위, application.yml에서 주입)
     */
    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration}") long validityInMilliseconds,
        TokenBlacklistService tokenBlacklistService
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public String createToken(String email, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
    }

    /**
     * 토큰을 블랙리스트에 추가 (로그아웃 시 사용)
     *
     * @param token 블랙리스트에 추가할 토큰
     */
    public void blacklistToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        tokenBlacklistService.addToBlacklist(token, claims.getExpiration());
    }

    /**
     * 토큰이 유효한지 검증
     *
     * @param token 검증할 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            if (tokenBlacklistService.isBlacklisted(token)) {
                return false;
            }
            
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}