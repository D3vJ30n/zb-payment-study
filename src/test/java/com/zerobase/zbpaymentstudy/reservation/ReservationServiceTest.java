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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
        reservationRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 회원이 정상적으로 예약을 생성할 수 있는지 테스트
     * 예약 생성 후 상태가 PENDING으로 설정되는지 확인
     */
    @Test
    @Transactional
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
    @Transactional
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
     * 점장이 예약을 승인할 수 있다
     */
    @Test
    @Transactional
    @DisplayName("점장이 예약을 승인할 수 있다")
    void approveReservation_Success() {
        // given
        Member owner = createPartnerMember("owner@test.com");
        Store store = createStore("테스트 매장", owner);
        Member customer = createMember("customer@test.com", "고객", "password");
        Reservation reservation = createPendingReservation(customer, store);

        // when
        ApiResponse<ReservationDto> response =
            reservationService.handleReservation(owner.getEmail(), reservation.getId(), true);

        // then
        assertThat(response.getResult()).isEqualTo("SUCCESS");
        assertThat(response.getData().status()).isEqualTo(ReservationStatus.APPROVED);
    }

    /**
     * 점장이 예약을 거절할 수 있다
     */
    @Test
    @Transactional
    @DisplayName("점장이 예약을 거절할 수 있다")
    void rejectReservation_Success() {
        // given
        Member owner = createPartnerMember("owner@test.com");
        Store store = createStore("테스트 매장", owner);
        Member customer = createMember("customer@test.com", "고객", "password");
        Reservation reservation = createPendingReservation(customer, store);

        // when
        ApiResponse<ReservationDto> response =
            reservationService.handleReservation(owner.getEmail(), reservation.getId(), false);

        // then
        assertThat(response.getResult()).isEqualTo("SUCCESS");
        assertThat(response.getData().status()).isEqualTo(ReservationStatus.REJECTED);
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
        Store store = Store.builder()
            .name(name)
            .owner(owner)
            .location("서울시 강남구")
            .description("테스트 매장")
            .latitude(37.123456)
            .longitude(127.123456)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return storeRepository.save(store);
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

    /**
     * 테스트용 대기중인 예약 생성 헬퍼 메서드
     *
     * @param member 예약 회원
     * @param store  예약 매장
     * @return 생성된 예약 엔티티
     */
    private Reservation createPendingReservation(Member member, Store store) {
        return reservationRepository.save(Reservation.builder()
            .member(member)
            .store(store)
            .reservationTime(LocalDateTime.now().plusDays(1))
            .status(ReservationStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    }

    /**
     * 파트너 회원 생성 헬퍼 메서드
     */
    private Member createPartnerMember(String email) {
        Member partner = Member.builder()
            .email(email)
            .password("password1234")
            .name("파트너")
            .role(MemberRole.PARTNER)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return memberRepository.save(partner);
    }
}