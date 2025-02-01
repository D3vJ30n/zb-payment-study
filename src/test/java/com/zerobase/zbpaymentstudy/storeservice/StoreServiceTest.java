package com.zerobase.zbpaymentstudy.storeservice;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreRegisterDto;
import com.zerobase.zbpaymentstudy.domain.store.service.StoreService;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional

/**
 * 매장 서비스 테스트 클래스
 * 매장 등록, 조회 등의 기능에 대한 통합 테스트를 수행
 */
class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 매장 등록이 정상적으로 되는지 테스트
     * 파트너 회원의 매장 등록 요청이 성공하는지 확인
     */
    @Test
    @DisplayName("파트너 회원이 매장을 정상적으로 등록할 수 있다")
    void registerStore_Success() {
        // given
        String ownerEmail = "partner1@test.com";
        Member partner = createPartnerMember(ownerEmail);
        StoreRegisterDto registerDto = new StoreRegisterDto(
            "테스트 매장",
            "서울시 강남구",
            "테스트 매장입니다"
        );

        // when
        ApiResponse<StoreDto> response = storeService.registerStore(ownerEmail, registerDto);

        // then
        assertThat(response.getResult()).isEqualTo("SUCCESS");
        assertThat(response.getData().name()).isEqualTo("테스트 매장");
    }

    /**
     * 일반 회원의 매장 등록 시도 시 예외 발생 테스트
     * 파트너가 아닌 회원이 매장 등록 시 BusinessException이 발생하는지 확인
     */
    @Test
    @DisplayName("일반 회원은 매장을 등록할 수 없다")
    void registerStore_Fail_NotPartner() {
        // given
        String userEmail = "user1@test.com";
        Member user = createGeneralMember(userEmail);
        StoreRegisterDto registerDto = new StoreRegisterDto(
            "테스트 매장",
            "서울시 강남구",
            "테스트 매장입니다"
        );

        // when & then
        assertThrows(BusinessException.class, () ->
            storeService.registerStore(userEmail, registerDto));
    }

    /**
     * 파트너 회원 생성 헬퍼 메서드
     *
     * @param email 파트너 회원 이메일
     * @return 생성된 파트너 회원 엔티티
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

    /**
     * 일반 회원 생성 헬퍼 메서드
     *
     * @param email 일반 회원 이메일
     * @return 생성된 일반 회원 엔티티
     */
    private Member createGeneralMember(String email) {
        Member user = Member.builder()
            .email(email)
            .password("password1234")
            .name("일반회원")
            .role(MemberRole.USER)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        return memberRepository.save(user);
    }
}