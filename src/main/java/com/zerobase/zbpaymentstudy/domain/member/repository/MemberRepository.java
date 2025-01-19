package com.zerobase.zbpaymentstudy.domain.member.repository;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 주어진 이메일로 회원이 존재하는지 확인
     *
     * @param email 조회할 회원의 이메일
     * @return 이메일이 존재하면 true, 존재하지 않으면 false 반환
     */
    boolean existsByEmail(String email);

    /**
     * 주어진 이메일로 회원 정보를 조회
     *
     * @param email 조회할 회원의 이메일
     * @return 회원 정보를 Optional로 감싸서 반환, 존재하지 않을 경우 Optional.empty() 반환
     */
    Optional<Member> findByEmail(String email);
} 