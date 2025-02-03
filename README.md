```markdown
# 🏪 매장 예약 및 리뷰 관리 시스템

## 📝 프로젝트 소개

이 프로젝트는 매장 예약 및 리뷰 관리를 위한 REST API 서비스입니다.

### 핵심 기능

- 파트너(점장): 매장 등록/관리 및 예약 승인/거절
- 일반 사용자: 매장 검색, 예약, 체크인, 리뷰 작성

---

## ⚙️ 개발 환경

- 언어: Java 17
- 프레임워크: Spring Boot 3.3.7
- 데이터베이스: MySQL 8.0
- 빌드 도구: Gradle
- 테스트: JUnit5, Mockito
- 기타 라이브러리
    - Spring Data JPA
    - QueryDSL (동적 쿼리 처리)
    - JWT (인증/인가)
    - Lombok (코드 간소화)

---

## 🔍 주요 기능

### 1️⃣ 회원가입 및 매장 등록

- 회원 권한 관리
    - USER: 일반 사용자
    - PARTNER: 매장 관리자(점장)
- JWT 기반 인증
    - Access Token 발급/검증
    - 권한별 API 접근 제어
- 매장 관리 (PARTNER 전용)
    - 매장 등록/수정/삭제
    - 매장 정보: 이름, 위치, 설명, 운영시간 등

### 2️⃣ 매장 검색 및 예약

- 매장 검색 기능
    - 키워드 검색 (매장명, 위치)
    - 정렬 옵션
        - 가나다순 ✅
        - 별점순 ✅ (평균 별점 기준)
        - 거리순 ✅ (현재 위치 기준)
    - 동적 검색 조건 (QueryDSL)
    - 페이징 처리
- 예약 시스템
    - 예약 생성 → 점장 승인 필요
    - 예약 상태 관리
        - PENDING: 승인 대기
        - APPROVED: 승인됨
        - REJECTED: 거절됨
        - CHECKED_IN: 체크인 완료
        - COMPLETED: 이용 완료

### 매장 검색 API 예시

- 정렬 옵션
    - `sort=name,asc`: 매장명 오름차순
    - `sort=rating,desc`: 별점 높은순
    - `sort=distance,asc`: 가까운 순 (위치 정보 필요)
        - `latitude`: 현재 위도
        - `longitude`: 현재 경도

### 3️⃣ 체크인 및 리뷰

- 체크인 시스템
    - 예약 시간 10분 전부터 체크인 가능
    - 키오스크 인증 코드 검증
- 리뷰 시스템
    - 이용 완료 후 리뷰 작성 가능
    - 별점 및 텍스트 리뷰
    - 권한 관리
        - 수정: 작성자만 가능
        - 삭제: 작성자 또는 매장 관리자

---

## 📋 API 명세

### 회원 API

- 회원가입: `POST /api/members/signup`
    - Request: 이메일, 비밀번호, 이름, 역할(USER/PARTNER)
- 로그인: `POST /api/members/login`
    - Response: JWT 토큰

### 매장 API

- 매장 등록: `POST /api/stores`
- 매장 목록 조회: `GET /api/stores`
    - Query Parameters:
        - keyword: 검색어
        - sort: 정렬 기준
        - page: 페이지 번호
        - size: 페이지 크기
- 매장 상세 조회: `GET /api/stores/{id}`

### 예약 API

- 예약 생성: `POST /api/reservations`
- 예약 승인/거절: `PATCH /api/reservations/{id}`
- 체크인: `POST /api/reservations/check-in`

### 리뷰 API

- 리뷰 작성: `POST /api/reviews`
- 리뷰 수정: `PATCH /api/reviews/{id}`
- 리뷰 삭제: `DELETE /api/reviews/{id}`
- 매장별 리뷰 조회: `GET /api/reviews/stores/{id}`

---

## 🏗️ 프로젝트 구조

```plaintext
src
├── main
│   ├── java
│   │   └── com.zerobase.zbpaymentstudy
│   │       ├── common      // 공통 기능 (예: 응답 형식, 예외 처리)
│   │       ├── config      // 설정 파일 (예: JWT, Security)
│   │       ├── domain      // 도메인별 기능 (회원, 매장, 예약, 리뷰)
│   │       │   ├── member       // 회원 관리
│   │       │   ├── reservation  // 예약 관리
│   │       │   ├── review       // 리뷰 관리
│   │       │   └── store        // 매장 관리
│   │       └── exception   // 커스텀 예외 처리
│   └── resources               // 설정 파일 및 리소스
└── test                        // 테스트 코드
```

---

## 📊 시스템 아키텍처

### 계층 구조

![Architecture Diagram](docs/images/architecture.png)

시스템은 다음과 같은 계층 구조로 설계되었습니다.

- Presentation Layer: REST API 엔드포인트 제공 및 요청/응답 처리
- Business Layer: 핵심 비즈니스 로직 및 트랜잭션 관리
- Persistence Layer: 데이터베이스 연산 및 데이터 접근
- Domain Layer: 비즈니스 엔티티 및 규칙 정의

### 예약 프로세스 흐름

![Sequence Diagram](docs/images/sequence.png)

예약부터 리뷰까지의 전체 프로세스를 시각화하여 표현하였습니다.

---

## 💾 ERD 설계

![ERD Diagram](docs/images/erd.png)

### 주요 엔티티

- Member: 사용자 정보 관리 (일반 사용자/파트너)
- Store: 매장 정보 관리
- Reservation: 예약 정보 및 상태 관리
- Review: 리뷰 정보 관리

### 핵심 관계

- Member(1) - Store(N): 파트너는 여러 매장을 소유할 수 있음
- Store(1) - Reservation(N): 매장은 여러 예약을 가질 수 있음
- Reservation(1) - Review(1): 하나의 예약당 하나의 리뷰 작성 가능

---

## 🚀 시작하기

### 요구사항

- Java 17
- MySQL 8.0

### 실행 방법

1. 저장소 클론
   ```bash
   git clone https://github.com/your-username/store-reservation.git
   ```

2. 데이터베이스 설정
    - `application.yml` 파일에 데이터베이스 정보를 입력합니다.
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/your_database
       username: your_username
       password: your_password
   ```

3. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

---

## 🧪 테스트

테스트를 실행하려면 다음 명령어를 사용하세요.

```bash
./gradlew test
```

---

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.  
자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

---

## 주요 코드 예시

### 예약 서비스 구현

```java

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Override
    public ApiResponse<ReservationDto> createReservation(String memberEmail, ReservationCreateDto dto) {
        try {
            Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

            Store store = storeRepository.findById(dto.storeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.STORE_NOT_FOUND));

            validateReservationTime(dto.reservationTime());

            Reservation reservation = Reservation.builder()
                .store(store)
                .member(member)
                .reservationTime(dto.reservationTime())
                .status(ReservationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            Reservation savedReservation = reservationRepository.save(reservation);
            log.info("예약 생성 완료 - memberEmail: {}, storeId: {}", memberEmail, dto.storeId());

            return new ApiResponse<>("SUCCESS", "예약이 생성되었습니다.", ReservationDto.from(savedReservation));
        } catch (BusinessException e) {
            log.warn("예약 생성 실패 - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("예약 생성 중 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
```

### 리뷰 시스템 구현

```java

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ApiResponse<ReviewDto> createReview(String memberEmail, ReviewCreateDto dto) {
        try {
            Reservation reservation = reservationRepository.findById(dto.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

            validateReviewCreation(reservation, memberEmail);

            Review review = Review.builder()
                .reservation(reservation)
                .rating(dto.rating())
                .content(dto.content())
                .createdAt(LocalDateTime.now())
                .build();

            Review savedReview = reviewRepository.save(review);
            return new ApiResponse<>("SUCCESS", "리뷰가 작성되었습니다.", ReviewDto.from(savedReview));
        } catch (BusinessException e) {
            throw e;
        }
    }
}
```