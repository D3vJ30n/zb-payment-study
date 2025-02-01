package com.zerobase.zbpaymentstudy.domain.store.repository;

import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 매장 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository와 StoreRepositoryCustom을 상속받아 기본 CRUD 및 커스텀 쿼리 기능을 제공
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    // 기본 CRUD 메소드는 JpaRepository에서 제공
}