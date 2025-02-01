package com.zerobase.zbpaymentstudy.reservation;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationCreateDto;
import com.zerobase.zbpaymentstudy.domain.reservation.dto.ReservationDto;
import com.zerobase.zbpaymentstudy.domain.reservation.entity.Reservation;
import com.zerobase.zbpaymentstudy.domain.reservation.repository.ReservationRepository;
import com.zerobase.zbpaymentstudy.domain.reservation.service.ReservationService;
import com.zerobase.zbpaymentstudy.domain.reservation.type.ReservationStatus;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import com.zerobase.zbpaymentstudy.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 예약 서비스 테스트 클래스
 * 예약 생성 및 체크인 기능에 대한 통합 테스트를 수행
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * 회원이 정상적으로 예약을 생성할 수 있는지 테스트
     * 예약 생성 후 상태가 PENDING으로 설정되는지 확인
     */
    @Test
    @DisplayName("회원이 정상적으로 예약을 생성할 수 있다")
    void createReservation_Success() {
        // given
        Member member = createMember("user@test.com", "testuser", "password");
        Store store = createStore("테스트 매장", member);
        ReservationCreateDto createDto = new ReservationCreateDto(
            store.getId(),
            LocalDateTime.now().plusDays(1)
        );

        // when
        ApiResponse<ReservationDto> response =
            reservationService.createReservation(member.getEmail(), createDto);

        // then
        assertThat(response.getResult()).isEqualTo("SUCCESS");
        assertThat(response.getData().status()).isEqualTo(ReservationStatus.PENDING);
    }

    /**
     * 예약 시간 10분 전에 체크인이 가능한지 테스트
     * 체크인 후 상태가 CHECKED_IN으로 변경되는지 확인
     */
    @Test
    @DisplayName("예약 시간 10분 전에 체크인할 수 있다")
    void checkIn_Success() {
        // given
        Member member = createMember("user@test.com", "testuser", "password");
        Store store = createStore("테스트 매장", member);
        Reservation reservation = createApprovedReservation(
            member,
            store,
            LocalDateTime.now().plusMinutes(5)
        );

        // when
        ApiResponse<ReservationDto> response =
            reservationService.checkIn(reservation.getId(), "1234");

        // then
        assertThat(response.getResult()).isEqualTo("SUCCESS");
        assertThat(response.getData().status()).isEqualTo(ReservationStatus.CHECKED_IN);
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
            .name(name)
            .password(password)
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
     * 테스트용 승인된 예약 생성 헬퍼 메서드
     *
     * @param member          예약 회원
     * @param store           예약 매장
     * @param reservationTime 예약 시간
     * @return 생성된 예약 엔티티
     */
    private Reservation createApprovedReservation(Member member, Store store, LocalDateTime reservationTime) {
        return reservationRepository.save(Reservation.builder()
            .member(member)
            .store(store)
            .reservationTime(reservationTime)
            .status(ReservationStatus.APPROVED)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    }
}