package com.zerobase.zbpaymentstudy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.MDC;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* com.zerobase.zbpaymentstudy..*Controller.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            // MDC 활용하여 요청 추적 기능 추가
            // 요청/응답 데이터 로깅
            // 민감 정보 마스킹 처리
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {} executed in {} ms", 
                     joinPoint.getSignature(), executionTime);
            return result;
        } catch (Exception e) {
            log.error("Exception in {}", joinPoint.getSignature(), e);
            throw e;
        }
    }
} 