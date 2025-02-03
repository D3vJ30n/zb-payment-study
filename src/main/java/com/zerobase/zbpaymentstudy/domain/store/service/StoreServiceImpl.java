package com.zerobase.zbpaymentstudy.domain.store.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreRegisterDto;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import com.zerobase.zbpaymentstudy.domain.store.repository.StoreRepository;
import com.zerobase.zbpaymentstudy.exception.BusinessException;
import com.zerobase.zbpaymentstudy.exception.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @throws BusinessException 존재하지 않는 회원이거나 파트너 회원이 아닌 경우
     * @throws RuntimeException  매장 등록 처리 중 예외 발생 시
     *                           <p>
     *                           처리 과정:
     *                           1. 매장 소유자 존재 여부 및 권한 확인
     *                           2. 매장 엔티티 생성
     *                           3. 데이터베이스에 매장 정보 저장
     *                           4. 저장된 매장 정보를 DTO로 변환하여 반환
     */

    @Override
    public ApiResponse<StoreDto> registerStore(String ownerEmail, StoreRegisterDto registerDto) {
        try {
            Member owner = memberRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            if (owner.getRole() != MemberRole.PARTNER) {
                throw new BusinessException(ErrorCode.NOT_A_PARTNER);
            }

            Store store = Store.builder()
                .name(registerDto.name())
                .location(registerDto.location())
                .description(registerDto.description())
                .latitude(registerDto.latitude())
                .longitude(registerDto.longitude())
                .owner(owner)
                .averageRating(0.0)
                .reviewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            Store savedStore = storeRepository.save(store);
            log.info("매장 등록 완료 - storeName: {}, ownerEmail: {}", store.getName(), ownerEmail);
            
            return new ApiResponse<>("SUCCESS", "매장이 성공적으로 등록되었습니다.", StoreDto.from(savedStore));
        } catch (BusinessException e) {
            log.warn("매장 등록 실패 - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("매장 등록 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 매장 목록을 검색하는 메서드
     * 주어진 검색 조건과 페이징 정보에 따라 매장 목록을 조회
     *
     * @param pageable 페이징 정보
     * @param criteria 검색 조건
     * @return 검색된 매장 목록과 페이징 정보
     * @throws BusinessException 매장 목록 조회 중 오류 발생 시
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StoreDto> findStores(
        @NotNull Pageable pageable,
        @NotNull StoreSearchCriteria criteria
    ) {
        try {
            return storeRepository.searchStores(criteria, pageable)
                .map(StoreDto::from);
        } catch (Exception e) {
            log.error("매장 목록 조회 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
} 