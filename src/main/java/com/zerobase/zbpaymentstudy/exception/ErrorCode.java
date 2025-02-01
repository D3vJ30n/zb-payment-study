package com.zerobase.zbpaymentstudy.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 애플리케이션에서 사용되는 에러 코드를 정의하는 열거형 클래스
 * HTTP 상태 코드와 에러 메시지를 함께 관리
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * 공통 에러
     * 서버 내부 오류 및 잘못된 요청에 대한 에러 코드
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /**
     * 매장 관련 에러
     * 매장 조회 실패 및 권한 관련 에러 코드
     */
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "매장을 찾을 수 없습니다."),
    INVALID_STORE_OWNER(HttpStatus.FORBIDDEN, "매장의 소유자가 아닙니다."),

    /**
     * 회원 관련 에러
     * 회원 조회 실패 및 권한 관련 에러 코드
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    NOT_PARTNER_MEMBER(HttpStatus.FORBIDDEN, "파트너 회원만 가능합니다."),

    /**
     * 예약 관련 에러
     * 예약 조회, 상태 변경, 체크인 관련 에러 코드
     */
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),
    INVALID_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "잘못된 예약 시간입니다."),
    INVALID_STATUS_UPDATE(HttpStatus.BAD_REQUEST, "잘못된 상태 변경입니다."),
    INVALID_CHECKIN_STATUS(HttpStatus.BAD_REQUEST, "체크인이 불가능한 상태입니다."),
    EARLY_CHECKIN(HttpStatus.BAD_REQUEST, "아직 체크인 시간이 되지 않았습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증 코드입니다."),

    /**
     * 리뷰 관련 에러
     * 리뷰 조회, 작성 권한, 중복 작성 관련 에러 코드
     */
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    NOT_RESERVATION_OWNER(HttpStatus.FORBIDDEN, "예약자만 리뷰를 작성할 수 있습니다."),
    INVALID_REVIEW_STATUS(HttpStatus.BAD_REQUEST, "예약 완료 후에만 리뷰를 작성할 수 있습니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 리뷰가 작성되었습니다.");

    /**
     * HTTP 상태 코드
     */
    private final HttpStatus status;

    /**
     * 에러 메시지
     */
    private final String message;
}