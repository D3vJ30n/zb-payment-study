package com.zerobase.zbpaymentstudy.domain.store.repository;

import com.zerobase.zbpaymentstudy.domain.member.entity.Member;
import com.zerobase.zbpaymentstudy.domain.member.repository.MemberRepository;
import com.zerobase.zbpaymentstudy.domain.member.type.MemberRole;
import com.zerobase.zbpaymentstudy.domain.store.dto.StoreSearchCriteria;
import com.zerobase.zbpaymentstudy.domain.store.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StoreRepositoryCustomImplTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member owner;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    void setUp() {
        // 기존 데이터 정리
        storeRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();

        // 테스트용 점주 생성
        owner = memberRepository.save(Member.builder()
            .email("owner@test.com")
            .password("password")
            .name("점주")
            .role(MemberRole.PARTNER)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());

        entityManager.flush();
        entityManager.clear();

        // 테스트용 매장 데이터 생성
        List<Store> stores = List.of(
            Store.builder()
                .name("분위기 좋은 레스토랑")
                .location("서울시 강남구")
                .latitude(37.4956)
                .longitude(127.0264)
                .averageRating(4.8)
                .reviewCount(15)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),
            Store.builder()
                .name("맛있는 식당")
                .location("서울시 강남구")
                .latitude(37.4967)
                .longitude(127.0276)
                .averageRating(4.5)
                .reviewCount(10)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),
            Store.builder()
                .name("동네 맛집")
                .location("서울시 강동구")
                .latitude(37.5301)
                .longitude(127.1235)
                .averageRating(4.2)
                .reviewCount(8)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build(),
            Store.builder()
                .name("멋진 카페")
                .location("서울시 서초구")
                .latitude(37.4923)
                .longitude(127.0292)
                .averageRating(4.0)
                .reviewCount(5)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        storeRepository.saveAll(stores);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @Transactional
    @DisplayName("키워드로 매장 검색 - 이름 기준")
    void searchByNameKeyword() {
        // given
        StoreSearchCriteria criteria = new StoreSearchCriteria(
            "맛있는",                // keyword
            null,                   // latitude
            null,                   // longitude
            null,                   // ownerEmail
            "NAME",                 // sortBy
            "ASC"                   // sortDirection
        );
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Store> result = storeRepository.searchStores(criteria, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).contains("맛있는");
    }

    @Test
    @Transactional
    @DisplayName("키워드로 매장 검색 - 위치 기준")
    void searchByLocationKeyword() {
        // given
        StoreSearchCriteria criteria = new StoreSearchCriteria(
            "강남구",                // keyword
            null,                   // latitude
            null,                   // longitude
            null,                   // ownerEmail
            "NAME",                 // sortBy
            "ASC"                   // sortDirection
        );
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Store> result = storeRepository.searchStores(criteria, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
            .allMatch(store -> store.getLocation().contains("강남구"));
    }

    @Test
    @Transactional
    @DisplayName("평점 기준 정렬 테스트")
    void searchWithRatingSort() {
        // given
        StoreSearchCriteria criteria = new StoreSearchCriteria(
            null,                   // keyword
            null,                   // latitude
            null,                   // longitude
            null,                   // ownerEmail
            "RATING",               // sortBy
            "DESC"                  // sortDirection
        );
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Store> result = storeRepository.searchStores(criteria, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(4);
        assertThat(result.getContent().get(0).getAverageRating()).isEqualTo(4.8);
        // 전체 정렬 순서도 확인
        assertThat(result.getContent())
            .extracting(Store::getAverageRating)
            .containsExactly(4.8, 4.5, 4.2, 4.0);
    }

    @Test
    @Transactional
    @DisplayName("점주 이메일로 매장 검색")
    void searchByOwnerEmail() {
        // given
        StoreSearchCriteria criteria = new StoreSearchCriteria(
            null,                   // keyword
            null,                   // latitude
            null,                   // longitude
            "owner@test.com",       // ownerEmail
            null,                   // sortBy
            null                    // sortDirection
        );
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Store> result = storeRepository.searchStores(criteria, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(4);
        assertThat(result.getContent())
            .allMatch(store -> store.getOwner().getEmail().equals("owner@test.com"));
    }

    @Test
    @Transactional
    @DisplayName("페이징 처리 테스트")
    void testPagination() {
        // given
        StoreSearchCriteria criteria = new StoreSearchCriteria(
            null,                   // keyword
            null,                   // latitude
            null,                   // longitude
            null,                   // ownerEmail
            null,                   // sortBy
            null                    // sortDirection
        );
        PageRequest pageRequest = PageRequest.of(0, 2);

        // when
        Page<Store> result = storeRepository.searchStores(criteria, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("정렬 조건 테스트")
    void testSorting() {
        // given
        StoreSearchCriteria criteria = new StoreSearchCriteria(
            null,                   // keyword
            null,                   // latitude
            null,                   // longitude
            null,                   // ownerEmail
            "NAME",                 // sortBy
            "ASC"                   // sortDirection
        );
        PageRequest pageRequest = PageRequest.of(0, 10,
            Sort.by(Sort.Direction.ASC, "name"));

        // when
        Page<Store> result = storeRepository.searchStores(criteria, pageRequest);

        // then
        assertThat(result.getContent()).hasSize(4);
        assertThat(result.getContent())
            .isSortedAccordingTo((s1, s2) -> s1.getName().compareTo(s2.getName()));
    }
} 