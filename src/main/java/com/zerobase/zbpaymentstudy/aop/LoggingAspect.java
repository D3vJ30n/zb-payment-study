package com.zerobase.zbpaymentstudy.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 컨트롤러 메서드의 실행을 로깅하는 AOP 클래스
 * <p>
 * 주요 사용 사례
 * 1. 성능 모니터링
 * - 각 API 엔드포인트의 응답 시간 측정
 * - 성능 병목 지점 식별
 * 예) "GET /api/stores/1 executed in 150ms"
 * <p>
 * 2. 에러 추적
 * - 예외 발생 시 상세 로그 기록
 * - 문제 해결을 위한 디버깅 정보 제공
 * 예) "Exception in StoreController.getStore: Store not found with id: 1"
 * <p>
 * 3. 보안 감사
 * - API 호출 기록 유지
 * - 비정상적인 접근 패턴 감지
 * 예) "Unauthorized access attempt to /api/stores/delete by user: anonymous"
 * <p>
 * 4. 트래픽 분석
 * - API별 사용 빈도 측정
 * - 리소스 사용량 모니터링
 * 예) "High traffic detected: 1000 requests/min on /api/stores/search"
 */
@Aspect         // AOP 기능을 제공하는 클래스임을 명시
@Component      // 스프링 빈으로 등록
@Slf4j         // Lombok의 로깅 기능 사용
public class LoggingAspect {

    /**
     * 컨트롤러 메서드 실행을 감싸는 Around 어드바이스
     * 메서드 실행 전/후의 시간을 측정하고 예외 발생을 로깅
     *
     * @param joinPoint 대상 메서드의 실행 정보를 담고 있는 객체
     * @return 대상 메서드의 실행 결과
     * @throws Throwable 메서드 실행 중 발생할 수 있는 모든 예외
     */
    @Around("execution(* com.zerobase.zbpaymentstudy..*Controller.*(..))")
    // 모든 컨트롤러 메서드를 대상으로 함
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis(); // 메서드 실행 시작 시간 기록
        try {
            // TODO: MDC(Mapped Diagnostic Context)를 활용한 요청 추적 기능 구현
            // TODO: 요청/응답 데이터 로깅 구현
            // TODO: 개인정보 등 민감 정보 마스킹 처리 구현

            Object result = joinPoint.proceed(); // 실제 메서드 실행

            // 메서드 실행 시간 계산 및 로깅
            long executionTime = System.currentTimeMillis() - start;
            log.info("Method {} executed in {} ms",
                joinPoint.getSignature(), // 실행된 메서드의 시그니처
                executionTime            // 실행 소요 시간
            );

            return result;
        } catch (Exception e) {
            // 예외 발생 시 로깅
            log.error("Exception in {}", joinPoint.getSignature(), e);
            throw e; // 예외를 다시 던져서 정상적인 예외 처리 흐름 유지
        }
    }
}