package com.zerobase.zbpaymentstudy.domain.member.service;

import com.zerobase.zbpaymentstudy.common.ApiResponse;
import com.zerobase.zbpaymentstudy.domain.member.dto.MemberDto;
import com.zerobase.zbpaymentstudy.domain.member.dto.MemberSignUpDto;
import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 회원 서비스의 구현체 클래스
 * 회원 관련 비즈니스 로직을 실제로 처리하는 클래스
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    /**
     * 회원 정보 데이터베이스 접근을 위한 리포지토리
     */
    private final MemberRepository memberRepository;

    /**
     * 비밀번호 암호화를 위한 인코더
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리를 수행하는 메서드
     *
     * @param signUpDto 회원가입 요청 정보를 담은 DTO
     * @return ApiResponse<MemberDto> 회원가입 결과 및 생성된 회원 정보
     * @throws IllegalArgumentException 이미 존재하는 이메일로 가입 시도할 경우
     * @throws RuntimeException         회원가입 처리 중 예외 발생 시
     *                                  <p>
     *                                  처리 과정:
     *                                  1. 이메일 중복 체크
     *                                  2. 회원 엔티티 생성 (비밀번호 암호화 포함)
     *                                  3. 데이터베이스에 회원 정보 저장
     *                                  4. 저장된 회원 정보를 DTO로 변환하여 반환
     */
    @Override
    public ApiResponse<MemberDto> signUp(MemberSignUpDto signUpDto) {
        try {
            if (memberRepository.existsByEmail(signUpDto.email())) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }

            Member member = Member.builder()
                .email(signUpDto.email())
                .password(passwordEncoder.encode(signUpDto.password()))
                .name(signUpDto.name())
                .role(signUpDto.role())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            Member savedMember = memberRepository.save(member);
            log.info("회원가입 완료 - email: {}, role: {}", member.getEmail(), member.getRole());
            return new ApiResponse<>("SUCCESS", "회원가입이 완료되었습니다.",
                new MemberDto(savedMember));
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생", e);
            throw new RuntimeException("회원가입 중 오류가 발생했습니다.", e);
        }
    }
} 