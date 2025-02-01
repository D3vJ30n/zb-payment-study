package com.zerobase.zbpaymentstudy.security;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Date;

/**
 * 로그아웃된 JWT 토큰을 관리하는 서비스
 * 블랙리스트에 등록된 토큰은 더 이상 유효하지 않은 것으로 간주
 */
@Service
public class TokenBlacklistService {
    private final ConcurrentMap<String, Date> blacklist = new ConcurrentHashMap<>();

    /**
     * 토큰을 블랙리스트에 추가
     *
     * @param token 블랙리스트에 추가할 토큰
     * @param expiryDate 토큰의 만료 시간
     */
    public void addToBlacklist(String token, Date expiryDate) {
        blacklist.put(token, expiryDate);
        removeExpiredTokens();
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인
     *
     * @param token 확인할 토큰
     * @return 블랙리스트에 있으면 true, 없으면 false
     */
    public boolean isBlacklisted(String token) {
        removeExpiredTokens();
        return blacklist.containsKey(token);
    }

    /**
     * 만료된 토큰을 블랙리스트에서 제거
     */
    private void removeExpiredTokens() {
        Date now = new Date();
        blacklist.entrySet().removeIf(entry -> entry.getValue().before(now));
    }
} 