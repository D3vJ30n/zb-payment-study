package com.zerobase.zbpaymentstudy.controller;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationCreateDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.reservation.service.ReservationService;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 예약 관련 API를 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 생성 API
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReservationDto>> createReservation(
        @AuthenticationPrincipal String memberEmail,
        @RequestBody @Valid ReservationCreateDto createDto
    ) {
        log.info("예약 생성 요청 - memberEmail: {}, storeId: {}", memberEmail, createDto.storeId());
        return ResponseEntity.ok(reservationService.createReservation(memberEmail, createDto));
    }

    /**
     * 예약 상태 변경 API
     */
    @PatchMapping("/{reservationId}/status")
    public ResponseEntity<ApiResponse<ReservationDto>> updateReservationStatus(
        @PathVariable Long reservationId,
        @RequestParam ReservationStatus status
    ) {
        log.info("예약 상태 변경 요청 - reservationId: {}, status: {}", reservationId, status);
        return ResponseEntity.ok(reservationService.updateReservationStatus(reservationId, status));
    }

    /**
     * 예약 체크인 API
     */
    @PostMapping("/{reservationId}/check-in")
    public ResponseEntity<ApiResponse<ReservationDto>> checkIn(
        @PathVariable Long reservationId,
        @RequestParam String verificationCode
    ) {
        log.info("체크인 요청 - reservationId: {}", reservationId);
        return ResponseEntity.ok(reservationService.checkIn(reservationId, verificationCode));
    }

    /**
     * 예약 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<Page<ReservationDto>> getReservations(
        ReservationSearchCriteria criteria,
        Pageable pageable
    ) {
        log.info("예약 목록 조회 요청 - criteria: {}, pageable: {}", criteria, pageable);
        return ResponseEntity.ok(reservationService.getReservations(criteria, pageable));
    }

    /**
     * 예약 처리 API
     */
    @PatchMapping("/{reservationId}/handle")
    public ResponseEntity<ApiResponse<ReservationDto>> handleReservation(
        @AuthenticationPrincipal String ownerEmail,
        @PathVariable Long reservationId,
        @RequestParam boolean approved
    ) {
        log.info("예약 처리 요청 - reservationId: {}, approved: {}", reservationId, approved);
        return ResponseEntity.ok(reservationService.handleReservation(ownerEmail, reservationId, approved));
    }
} 