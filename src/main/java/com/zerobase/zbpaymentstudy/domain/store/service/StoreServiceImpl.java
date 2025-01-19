package com.zerobase.zbpaymentstudy.domain.store.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreRegisterDto;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import com.zerobase.zbpaymentstudy.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 매장 서비스의 구현체 클래스
 * 매장 관련 비즈니스 로직을 실제로 처리
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;     // 매장 정보 관리를 위한 리포지토리
    private final MemberRepository memberRepository;    // 회원 정보 조회를 위한 리포지토리

    /**
     * 새로운 매장을 등록하는 메서드
     *
     * @param ownerEmail  매장 소유자의 이메일
     * @param registerDto 매장 등록 정보를 담은 DTO
     * @return ApiResponse<StoreDto> 매장 등록 결과 및 생성된 매장 정보
     * @throws IllegalArgumentException 존재하지 않는 회원이거나 파트너 회원이 아닌 경우
     * @throws RuntimeException         매장 등록 처리 중 예외 발생 시
     *                                  <p>
     *                                  처리 과정:
     *                                  1. 매장 소유자 존재 여부 및 권한 확인
     *                                  2. 매장 엔티티 생성
     *                                  3. 데이터베이스에 매장 정보 저장
     *                                  4. 저장된 매장 정보를 DTO로 변환하여 반환
     */
    @Override
    public ApiResponse<StoreDto> registerStore(String ownerEmail, StoreRegisterDto registerDto) {
        try {
            var owner = memberRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

            if (owner.getRole() != MemberRole.PARTNER) {
                throw new IllegalArgumentException("매장 등록은 파트너 회원만 가능합니다.");
            }

            Store store = Store.builder()
                .name(registerDto.name())
                .location(registerDto.location())
                .description(registerDto.description())
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            Store savedStore = storeRepository.save(store);
            log.info("매장 등록 완료 - storeName: {}, ownerEmail: {}", store.getName(), ownerEmail);
            return new ApiResponse<>("SUCCESS", "매장이 등록되었습니다.", new StoreDto(savedStore));
        } catch (Exception e) {
            log.error("매장 등록 중 오류 발생", e);
            throw new RuntimeException("매장 등록 중 오류가 발생했습니다.", e);
        }
    }
} 