package com.zerobase.zbpaymentstudy.controller;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewCreateDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewUpdateDto;
import com.zerobase.zbpaymentstudy.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 리뷰 관련 API를 처리하는 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 작성 API
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
        @AuthenticationPrincipal String memberEmail,
        @RequestBody @Valid ReviewCreateDto createDto
    ) {
        log.info("리뷰 작성 요청 - memberEmail: {}, reservationId: {}", memberEmail, createDto.reservationId());
        return ResponseEntity.ok(reviewService.createReview(memberEmail, createDto));
    }

    /**
     * 리뷰 수정 API
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
        @PathVariable Long reviewId,
        @RequestBody @Valid ReviewUpdateDto updateDto
    ) {
        log.info("리뷰 수정 요청 - reviewId: {}", reviewId);
        return ResponseEntity.ok(reviewService.updateReview(reviewId, updateDto));
    }

    /**
     * 리뷰 삭제 API
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
        @PathVariable Long reviewId
    ) {
        log.info("리뷰 삭제 요청 - reviewId: {}", reviewId);
        return ResponseEntity.ok(reviewService.deleteReview(reviewId));
    }

    /**
     * 매장별 리뷰 목록 조회 API
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<Page<ReviewDto>> getStoreReviews(
        @PathVariable Long storeId,
        Pageable pageable
    ) {
        log.info("매장 리뷰 목록 조회 요청 - storeId: {}", storeId);
        return ResponseEntity.ok(reviewService.getStoreReviews(storeId, pageable));
    }
} 