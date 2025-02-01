package com.zerobase.zbpaymentstudy.domain.review.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.reservation.repository.ReservationRepository;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewCreateDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewUpdateDto;
import com.zerobase.zbpaymentstudy.domain.review.entity.Review;
import com.zerobase.zbpaymentstudy.domain.review.repository.ReviewRepository;
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
 * 리뷰 서비스의 구현 클래스
 * 리뷰와 관련된 모든 비즈니스 로직을 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 새로운 리뷰를 생성하는 메서드
     * 예약 정보 확인 및 리뷰 작성 가능 여부를 검증한 후 리뷰를 생성
     *
     * @param memberEmail 리뷰를 작성하는 회원의 이메일
     * @param dto         리뷰 생성에 필요한 정보를 담은 DTO
     * @return 생성된 리뷰 정보를 포함한 API 응답
     * @throws BusinessException 예약을 찾을 수 없거나 리뷰 작성이 불가능한 경우
     */
    @Override
    public ApiResponse<ReviewDto> createReview(String memberEmail, ReviewCreateDto dto) {
        try {
            Reservation reservation = reservationRepository.findById(dto.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

            validateReviewCreation(reservation, memberEmail);

            Review review = Review.builder()
                .reservation(reservation)
                .rating(dto.rating())
                .content(dto.content())
                .createdAt(LocalDateTime.now())
                .build();

            Review savedReview = reviewRepository.save(review);
            return new ApiResponse<>("SUCCESS", "리뷰가 작성되었습니다.",
                ReviewDto.from(savedReview));
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 기존 리뷰를 수정하는 메서드
     * 리뷰 존재 여부를 확인하고 내용을 업데이트
     *
     * @param reviewId 수정할 리뷰의 ID
     * @param dto      수정할 리뷰 정보를 담은 DTO
     * @return 수정된 리뷰 정보를 포함한 API 응답
     * @throws BusinessException 리뷰를 찾을 수 없거나 수정 권한이 없는 경우
     */
    @Override
    public ApiResponse<ReviewDto> updateReview(Long reviewId, ReviewUpdateDto dto) {
        try {
            Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

            review.setRating(dto.rating());
            review.setContent(dto.content());
            review.setUpdatedAt(LocalDateTime.now());

            Review updatedReview = reviewRepository.save(review);
            log.info("리뷰 수정 완료 - reviewId: {}", reviewId);

            return new ApiResponse<>("SUCCESS", "리뷰가 수정되었습니다.", ReviewDto.from(updatedReview));
        } catch (BusinessException e) {
            log.warn("리뷰 수정 실패 - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("리뷰 수정 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 리뷰를 삭제하는 메서드
     * 리뷰 존재 여부를 확인하고 삭제 처리
     *
     * @param reviewId 삭제할 리뷰의 ID
     * @return 삭제 결과를 포함한 API 응답
     * @throws BusinessException 리뷰를 찾을 수 없거나 삭제 권한이 없는 경우
     */
    @Override
    public ApiResponse<Void> deleteReview(Long reviewId) {
        try {
            Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND));

            reviewRepository.delete(review);
            log.info("리뷰 삭제 완료 - reviewId: {}", reviewId);

            return new ApiResponse<>("SUCCESS", "리뷰가 삭제되었습니다.", null);
        } catch (BusinessException e) {
            log.warn("리뷰 삭제 실패 - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 매장의 리뷰 목록을 조회하는 메서드
     * 페이징 처리된 리뷰 목록을 반환
     *
     * @param storeId  리뷰를 조회할 매장의 ID
     * @param pageable 페이징 정보
     * @return 매장의 리뷰 목록과 페이징 정보
     * @throws BusinessException 조회 중 오류가 발생한 경우
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDto> getStoreReviews(Long storeId, Pageable pageable) {
        try {
            Page<Review> reviews = reviewRepository.findByStoreId(storeId, pageable);
            return reviews.map(ReviewDto::from);
        } catch (Exception e) {
            log.error("리뷰 조회 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 리뷰 생성 가능 여부를 검증하는 메서드
     * 예약 소유자 확인, 예약 상태 확인, 중복 리뷰 확인 등을 수행
     *
     * @param reservation 검증할 예약 정보
     * @param memberEmail 리뷰 작성 요청자의 이메일
     * @throws BusinessException 리뷰 작성이 불가능한 경우
     */
    private void validateReviewCreation(Reservation reservation, String memberEmail) {
        if (!reservation.getMember().getEmail().equals(memberEmail)) {
            throw new BusinessException(ErrorCode.NOT_RESERVATION_OWNER);
        }

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_REVIEW_STATUS);
        }

        if (reviewRepository.existsByReservationId(reservation.getId())) {
            throw new BusinessException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }
    }
} 