package com.zerobase.zbpaymentstudy.domain.review.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewCreateDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 리뷰 관련 비즈니스 로직을 정의하는 서비스 인터페이스
 * 리뷰의 생성, 수정, 삭제, 조회 등의 핵심 기능을 제공
 */
public interface ReviewService {
    /**
     * 새로운 리뷰를 생성하는 메서드
     * 예약 완료된 서비스에 대해서만 리뷰 작성이 가능
     *
     * @param memberEmail 리뷰를 작성하는 회원의 이메일
     * @param dto         리뷰 생성에 필요한 정보를 담은 DTO
     * @return 생성된 리뷰 정보를 포함한 API 응답
     */
    ApiResponse<ReviewDto> createReview(String memberEmail, ReviewCreateDto dto);

    /**
     * 기존 리뷰를 수정하는 메서드
     * 리뷰 작성자만 수정이 가능
     *
     * @param reviewId 수정할 리뷰의 ID
     * @param dto      수정할 리뷰 정보를 담은 DTO
     * @return 수정된 리뷰 정보를 포함한 API 응답
     */
    ApiResponse<ReviewDto> updateReview(Long reviewId, ReviewUpdateDto dto);

    /**
     * 리뷰를 삭제하는 메서드
     * 리뷰 작성자만 삭제가 가능
     *
     * @param reviewId 삭제할 리뷰의 ID
     * @return 삭제 결과를 포함한 API 응답
     */
    ApiResponse<Void> deleteReview(Long reviewId);

    /**
     * 특정 매장의 리뷰 목록을 조회하는 메서드
     * 페이징 처리를 지원하여 대량의 리뷰를 효율적으로 조회
     *
     * @param storeId  리뷰를 조회할 매장의 ID
     * @param pageable 페이징 정보
     * @return 매장의 리뷰 목록과 페이징 정보
     */
    Page<ReviewDto> getStoreReviews(Long storeId, Pageable pageable);
}