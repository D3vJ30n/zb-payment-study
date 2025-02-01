package com.zerobase.zbpaymentstudy.review;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.reservation.repository.ReservationRepository;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewCreateDto;
import com.zerobase.zbpaymentstudy.domain.review.dto.ReviewDto;
import com.zerobase.zbpaymentstudy.domain.review.service.ReviewService;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import com.zerobase.zbpaymentstudy.domain.store.repository.StoreRepository;
import com.zerobase.zbpaymentstudy.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 리뷰 서비스 테스트 클래스
 * 리뷰 생성 및 권한 검증에 대한 통합 테스트를 수행
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    /**
     * 예약 완료 후 리뷰 작성이 정상적으로 되는지 테스트
     * 리뷰 작성 후 평점이 정확하게 저장되는지 확인
     */
    @Test
    @DisplayName("예약 완료 후 리뷰를 작성할 수 있다")
    void createReview_Success() {
        // given
        Reservation reservation = createCompletedReservation("user1@test.com");
        ReviewCreateDto createDto = new ReviewCreateDto(
            reservation.getId(),
            5,
            "좋은 매장이었습니다!"
        );

        // when
        ApiResponse<ReviewDto> response =
            reviewService.createReview(reservation.getMember().getEmail(), createDto);

        // then
        assertThat(response.getResult()).isEqualTo("SUCCESS");
        assertThat(response.getData().rating()).isEqualTo(5);
    }

    /**
     * 예약자가 아닌 사용자의 리뷰 작성 시도 시 예외 발생 테스트
     * BusinessException이 발생하는지 확인
     */
    @Test
    @DisplayName("예약자가 아닌 사용자는 리뷰를 작성할 수 없다")
    void createReview_Fail_NotReservationOwner() {
        // given
        Reservation reservation = createCompletedReservation("user2@test.com");
        ReviewCreateDto createDto = new ReviewCreateDto(
            reservation.getId(),
            5,
            "좋은 매장이었습니다!"
        );

        String otherUserEmail = "other@test.com";
        createMember(otherUserEmail, "다른 사용자", "password1234");

        // when & then
        assertThrows(BusinessException.class, () ->
            reviewService.createReview(otherUserEmail, createDto));
    }

    /**
     * 테스트용 회원 생성 헬퍼 메서드
     *
     * @param email    회원 이메일
     * @param name     회원 이름
     * @param password 회원 비밀번호
     * @return 생성된 회원 엔티티
     */
    private Member createMember(String email, String name, String password) {
        return memberRepository.save(Member.builder()
            .email(email)
            .password(password)
            .name(name)
            .role(MemberRole.USER)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    }

    /**
     * 테스트용 매장 생성 헬퍼 메서드
     *
     * @param name  매장명
     * @param owner 매장 소유자
     * @return 생성된 매장 엔티티
     */
    private Store createStore(String name, Member owner) {
        return storeRepository.save(Store.builder()
            .name(name)
            .location("서울시 강남구")
            .description("테스트 매장입니다")
            .owner(owner)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    }

    /**
     * 테스트용 완료된 예약 생성 헬퍼 메서드
     *
     * @param userEmail 예약자 이메일
     * @return 생성된 완료 상태의 예약 엔티티
     */
    private Reservation createCompletedReservation(String userEmail) {
        Member member = createMember(userEmail, "테스트 회원", "password1234");
        Store store = createStore("테스트 매장", member);

        return reservationRepository.save(Reservation.builder()
            .member(member)
            .store(store)
            .reservationTime(LocalDateTime.now().minusDays(1))
            .status(ReservationStatus.COMPLETED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    }
}