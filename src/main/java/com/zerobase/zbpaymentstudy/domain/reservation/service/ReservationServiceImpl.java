package com.zerobase.zbpaymentstudy.domain.reservation.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationCreateDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.reservation.repository.ReservationRepository;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import com.zerobase.zbpaymentstudy.domain.store.repository.StoreRepository;
import com.zerobase.zbpaymentstudy.exception.BusinessException;
import com.zerobase.zbpaymentstudy.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 예약 서비스의 구현 클래스
 * 예약과 관련된 모든 비즈니스 로직을 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    /**
     * 새로운 예약을 생성하는 메서드
     * 회원과 매장 정보를 확인하고 예약 시간을 검증한 후 예약을 생성
     *
     * @param memberEmail 예약 요청 회원의 이메일
     * @param dto         예약 생성 정보
     * @return 생성된 예약 정보를 포함한 API 응답
     * @throws BusinessException 회원이나 매장을 찾을 수 없는 경우, 예약 시간이 유효하지 않은 경우
     */
    @Override
    public ApiResponse<ReservationDto> createReservation(String memberEmail, ReservationCreateDto dto) {
        try {
            Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

            Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

            validateReservationTime(dto.reservationTime());
            validateDuplicateReservation(member, store, dto.reservationTime());

            Reservation reservation = Reservation.builder()
                .member(member)
                .store(store)
                .reservationTime(dto.reservationTime())
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            Reservation savedReservation = reservationRepository.save(reservation);
            return new ApiResponse<>("SUCCESS", "예약이 생성되었습니다.", ReservationDto.from(savedReservation));
        } catch (BusinessException e) {
            log.warn("예약 생성 실패 - {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 예약 시간의 유효성을 검증하는 메서드
     * 예약 시간이 현재 시간보다 이전인 경우 예외 발생
     *
     * @param reservationTime 검증할 예약 시간
     * @throws BusinessException 예약 시간이 현재보다 이전인 경우
     */
    private void validateReservationTime(LocalDateTime reservationTime) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간보다 이전 시간으로 예약 불가
        if (reservationTime.isBefore(now)) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_TIME);
        }

        // 최소 1시간 전에 예약해야 함
        if (reservationTime.isBefore(now.plusHours(1))) {
            throw new BusinessException(ErrorCode.RESERVATION_TOO_CLOSE);
        }

        // 30일 이후 예약 불가
        if (reservationTime.isAfter(now.plusDays(30))) {
            throw new BusinessException(ErrorCode.RESERVATION_TOO_FAR);
        }

        // 영업 시간(10:00 ~ 22:00) 체크
        int hour = reservationTime.getHour();
        if (hour < 10 || hour >= 22) {
            throw new BusinessException(ErrorCode.OUTSIDE_BUSINESS_HOURS);
        }
    }

    private void validateDuplicateReservation(Member member, Store store, LocalDateTime reservationTime) {
        // 같은 시간대에 중복 예약 체크 (앞뒤 1시간)
        boolean hasOverlap = reservationRepository.existsByMemberAndReservationTimeBetween(
            member,
            reservationTime.minusHours(1),
            reservationTime.plusHours(1)
        );

        if (hasOverlap) {
            throw new BusinessException(ErrorCode.DUPLICATE_RESERVATION);
        }

        // 매장의 동시간대 예약 수 체크
        int maxReservationsPerHour = 5; // 시간당 최대 예약 수
        long reservationCount = reservationRepository.countByStoreAndReservationTimeBetween(
            store,
            reservationTime.minusMinutes(30),
            reservationTime.plusMinutes(30)
        );

        if (reservationCount >= maxReservationsPerHour) {
            throw new BusinessException(ErrorCode.STORE_FULLY_BOOKED);
        }
    }

    /**
     * 예약 상태를 업데이트하는 메서드
     * 예약의 현재 상태를 확인하고 새로운 상태로 변경
     *
     * @param reservationId 상태를 변경할 예약 ID
     * @param status        새로운 예약 상태
     * @return 업데이트된 예약 정보를 포함한 API 응답
     * @throws BusinessException 예약을 찾을 수 없거나 상태 변경이 불가능한 경우
     */
    @Override
    public ApiResponse<ReservationDto> updateReservationStatus(Long reservationId, ReservationStatus status) {
        try {
            Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

            validateStatusUpdate(reservation, status);

            reservation.setStatus(status);
            reservation.setUpdatedAt(LocalDateTime.now());

            Reservation updatedReservation = reservationRepository.save(reservation);
            return new ApiResponse<>("SUCCESS", "예약 상태가 변경되었습니다.",
                ReservationDto.from(updatedReservation));
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 키오스크를 통한 체크인 처리 메서드
     * 예약 상태와 인증 코드를 확인하고 체크인 처리
     *
     * @param reservationId    체크인할 예약 ID
     * @param verificationCode 예약 확인 인증 코드
     * @return 체크인 처리된 예약 정보를 포함한 API 응답
     * @throws BusinessException 예약을 찾을 수 없거나 체크인이 불가능한 경우
     */
    @Override
    public ApiResponse<ReservationDto> checkIn(Long reservationId, String verificationCode) {
        try {
            Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

            validateCheckInTime(reservation);
            validateVerificationCode(reservation, verificationCode);

            reservation.setStatus(ReservationStatus.CHECKED_IN);
            reservation.setCheckedInAt(LocalDateTime.now());

            Reservation updatedReservation = reservationRepository.save(reservation);

            // 매장 알림 로직 추가
            notifyStore(updatedReservation);

            return new ApiResponse<>("SUCCESS", "체크인이 완료되었습니다.",
                ReservationDto.from(updatedReservation));
        } catch (BusinessException e) {
            log.warn("체크인 실패 - {}", e.getMessage());
            throw e;
        }
    }

    private void validateCheckInTime(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationTime = reservation.getReservationTime();
        
        // 예약 시간 10분 전부터 체크인 가능
        if (now.isBefore(reservationTime.minusMinutes(10))) {
            throw new BusinessException(ErrorCode.EARLY_CHECKIN);
        }
        
        // 예약 시간 30분 후까지만 체크인 가능
        if (now.isAfter(reservationTime.plusMinutes(30))) {
            throw new BusinessException(ErrorCode.LATE_CHECKIN);
        }
    }

    private void notifyStore(Reservation reservation) {
        // TODO: 실제 알림 로직 구현 (웹소켓, FCM 등)
        log.info("매장 알림 전송 - 예약번호: {}, 매장: {}",
            reservation.getId(), reservation.getStore().getName());
    }

    /**
     * 예약 목록을 조회하는 메서드
     * 주어진 검색 조건과 페이징 정보에 따라 예약 목록을 조회
     *
     * @param criteria 검색 조건
     * @param pageable 페이징 정보
     * @return 예약 목록과 페이징 정보
     * @throws BusinessException 조회 중 오류가 발생한 경우
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservations(ReservationSearchCriteria criteria, Pageable pageable) {
        try {
            Page<Reservation> reservations = reservationRepository.findReservations(criteria, pageable);
            return reservations.map(ReservationDto::from);
        } catch (Exception e) {
            log.error("예약 조회 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 예약 상태 업데이트의 유효성을 검증하는 메서드
     * 이미 완료되거나 취소된 예약, 키오스크를 통하지 않은 체크인 시도 등을 검증
     *
     * @param reservation 검증할 예약 정보
     * @param newStatus   변경하려는 새로운 상태
     * @throws BusinessException 상태 변경이 불가능한 경우
     */
    private void validateStatusUpdate(Reservation reservation, ReservationStatus newStatus) {
        if (reservation.getStatus() == ReservationStatus.COMPLETED ||
            reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_UPDATE);
        }

        if (newStatus == ReservationStatus.CHECKED_IN) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_UPDATE,
                "체크인은 키오스크를 통해서만 가능합니다.");
        }
    }

    /**
     * 체크인 가능 여부를 검증하는 메서드
     * 예약 상태와 체크인 가능 시간을 확인
     *
     * @param reservation 검증할 예약 정보
     * @throws BusinessException 체크인이 불가능한 경우
     */
    private void validateCheckIn(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new BusinessException(ErrorCode.INVALID_CHECKIN_STATUS);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInStartTime = reservation.getReservationTime().minusMinutes(10);

        if (now.isBefore(checkInStartTime)) {
            throw new BusinessException(ErrorCode.EARLY_CHECKIN);
        }
    }

    /**
     * 체크인 인증 코드를 검증하는 메서드
     * 제공된 인증 코드와 저장된 인증 코드를 비교
     *
     * @param reservation      검증할 예약 정보
     * @param verificationCode 검증할 인증 코드
     * @throws BusinessException 인증 코드가 일치하지 않는 경우
     */
    private void validateVerificationCode(Reservation reservation, String verificationCode) {
        // 실제 구현에서는 예약 시 생성된 인증 코드와 비교
        if (!"1234".equals(verificationCode)) {  // 임시 코드
            throw new BusinessException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    @Override
    @Transactional
    public ApiResponse<ReservationDto> handleReservation(String ownerEmail, Long reservationId, boolean approved) {
        try {
            Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

            // 매장 소유자 검증
            if (!reservation.getStore().getOwner().getEmail().equals(ownerEmail)) {
                throw new BusinessException(ErrorCode.INVALID_STORE_OWNER);
            }

            // 예약 상태 검증
            if (reservation.getStatus() != ReservationStatus.PENDING) {
                throw new BusinessException(ErrorCode.INVALID_STATUS_UPDATE);
            }

            // 승인/거절 처리
            reservation.setStatus(approved ? ReservationStatus.APPROVED : ReservationStatus.REJECTED);
            reservation.setUpdatedAt(LocalDateTime.now());

            Reservation updatedReservation = reservationRepository.save(reservation);

            // 예약자에게 알림 발송 (이메일 또는 푸시 알림)
            notifyReservationResult(updatedReservation);

            String message = approved ? "예약이 승인되었습니다." : "예약이 거절되었습니다.";
            return new ApiResponse<>("SUCCESS", message, ReservationDto.from(updatedReservation));
        } catch (BusinessException e) {
            log.warn("예약 처리 실패 - {}", e.getMessage());
            throw e;
        }
    }

    private void notifyReservationResult(Reservation reservation) {
        // TODO: 실제 알림 로직 구현
        log.info("예약 결과 알림 발송 - 예약번호: {}, 상태: {}, 예약자: {}",
            reservation.getId(),
            reservation.getStatus(),
            reservation.getMember().getEmail());
    }
} 