# Backend Interview Tracker

**[한국어](./README.md)** · [English](./README.en.md) · [日本語](./README.ja.md)

백엔드 면접 준비를 위해 질문과 모범 답안을 카테고리별로 정리하고 이해도를 관리할 수 있는 REST API 서버입니다.  
Java, Spring Boot, JPA 기반의 CRUD API 설계와 예외 처리, 테스트 코드 작성을 직접 구현하며 백엔드 기본기를 다지는 것을 목표로 만들었습니다.

---

## 🛠️ 기술 스택

| 분류 | 사용 기술 |
|---|---|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.2.4 |
| 데이터베이스 | H2 (In-memory) |
| ORM | Spring Data JPA / Hibernate |
| 빌드 | Gradle 8.7 |
| 테스트 | JUnit 5, Mockito, MockMvc |
| 문서화 | Springdoc OpenAPI (Swagger UI) |
| 기타 | Lombok, Spring Validation |

---

## 📌 API 목록

| Method | URL | 설명 |
|---|---|---|
| POST | `/api/questions` | 질문 등록 |
| GET | `/api/questions/{id}` | 질문 단건 조회 |
| GET | `/api/questions` | 질문 목록 조회 (페이징, 필터, 정렬) |
| PUT | `/api/questions/{id}` | 질문 수정 |
| DELETE | `/api/questions/{id}` | 질문 삭제 |

### 쿼리 파라미터 (GET /api/questions)

| 파라미터 | 타입 | 기본값 | 설명 |
|---|---|---|---|
| `keyword` | String | - | 제목 또는 답변 키워드 검색 |
| `category` | Enum | - | `JAVA`, `SPRING`, `DB`, `NETWORK`, `OS`, `ETC` |
| `status` | Enum | - | `UNKNOWN`, `UNCERTAIN`, `MASTERED` |
| `page` | int | `0` | 페이지 번호 |
| `size` | int | `10` | 페이지 크기 |
| `sort` | String | `createdAt,desc` | 정렬 기준 (`필드명,asc\|desc`) |

---

## ⚙️ 로컬 실행 방법

### 1. 저장소 클론

```bash
git clone https://github.com/your-username/backend-interview-tracker.git
cd backend-interview-tracker
```

### 2. 테스트 실행

```bash
# Windows
.\gradlew.bat clean test

# macOS / Linux
./gradlew clean test
```

```text
> Task :test
BUILD SUCCESSFUL in 16s
5 actionable tasks: 5 executed
```

- `QuestionServiceTest` — 서비스 계층 비즈니스 로직 (Mockito)
- `QuestionControllerTest` — API 요청/응답, Validation, 예외 처리 (MockMvc)

### 3. 서버 실행

```bash
# Windows
.\gradlew.bat bootRun

# macOS / Linux
./gradlew bootRun
```

서버가 정상적으로 시작되면 콘솔에 아래 메시지가 출력됩니다.

```text
Tomcat started on port 8080 (http) with context path ''
```

### 4. 접속 경로

| 서비스 | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| API Docs (JSON) | `http://localhost:8080/v3/api-docs` |
| H2 콘솔 | `http://localhost:8080/h2-console` |

> H2 콘솔 접속 정보: JDBC URL `jdbc:h2:mem:interviewdb` / Username `sa` / Password 없음

---

## 📝 API 요청 예시

### 질문 등록 (POST /api/questions)

```bash
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "JVM이란 무엇인가요?",
    "answer": "JVM은 Java Virtual Machine의 약자로, 자바 바이트코드를 실행하는 가상 머신입니다.",
    "source": "backend-interview-question GitHub",
    "category": "JAVA",
    "status": "UNCERTAIN"
  }'
```

**응답**

```json
{
  "success": true,
  "message": "성공적으로 처리되었습니다.",
  "data": 1
}
```

### 질문 목록 조회 (GET /api/questions)

```bash
# 기본 조회 (최신순, 10개)
curl "http://localhost:8080/api/questions"

# 페이지, 정렬 지정
curl "http://localhost:8080/api/questions?page=0&size=10&sort=createdAt,desc"

# 카테고리 + 이해도 필터
curl "http://localhost:8080/api/questions?category=JAVA&status=UNCERTAIN"

# 키워드 검색
curl "http://localhost:8080/api/questions?keyword=JVM"
```

**응답**

```json
{
  "success": true,
  "message": "성공적으로 처리되었습니다.",
  "data": {
    "content": [ ... ],
    "totalElements": 5,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "first": true,
    "last": true,
    "empty": false
  }
}
```

### 질문 수정 (PUT /api/questions/{id})

```bash
curl -X PUT http://localhost:8080/api/questions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "JVM이란 무엇인가요?",
    "answer": "JVM은 자바 프로그램을 실행하기 위한 가상 머신으로, 플랫폼 독립성을 보장합니다.",
    "source": "자바 공식 문서",
    "category": "JAVA",
    "status": "MASTERED"
  }'
```

### 질문 삭제 (DELETE /api/questions/{id})

```bash
curl -X DELETE http://localhost:8080/api/questions/1
```

---

## 🔍 구현 포인트

- **공통 응답 포맷 (`ApiResponse<T>`):** 모든 API가 `success`, `message`, `data` 필드로 일관된 JSON을 반환합니다.
- **전역 예외 처리 (`GlobalExceptionHandler`):** Validation 실패, 잘못된 Enum 값, 존재하지 않는 정렬 필드 등 다양한 오류를 명확한 400/404 메시지로 응답합니다.
- **JPQL 기반 동적 검색:** `keyword`, `category`, `status`를 조건부로 조합하는 쿼리를 직접 작성했습니다.
- **JPA Auditing 분리:** `@WebMvcTest` 환경에서 `@EnableJpaAuditing` 의존성 충돌을 방지하기 위해 `JpaConfig`를 별도 클래스로 분리했습니다.

---

## 🔮 향후 개선 계획

1. **MySQL 마이그레이션** — H2 In-memory DB를 MySQL 8.0으로 전환
2. **Docker Compose 적용** — 애플리케이션과 DB를 컨테이너로 분리하여 환경 독립적으로 실행
3. **QueryDSL 도입** — 복잡한 조건 검색을 타입 안전하게 처리
4. **Spring Security + JWT** — 사용자 인증/인가 기능 추가, 개인별 질문 관리
5. **배포** — GitHub Actions CI/CD 구성 및 클라우드 서버(AWS EC2 또는 Railway) 배포

---

## ⚠️ 트러블슈팅

### Swagger `/v3/api-docs` 500 오류

`application.yml`에서 `springdoc.api-docs.path`를 `/api-docs`로 커스터마이징했을 때 발생했습니다.  
Swagger UI 프론트엔드는 기본 경로인 `/v3/api-docs`를 고정으로 호출하는데, 경로가 바뀌면서 리소스를 찾지 못해 `NoResourceFoundException`이 발생했습니다.  
이때 `GlobalExceptionHandler`의 최상위 `Exception` 핸들러가 이를 500으로 감싸버려 오해하기 쉬운 오류가 출력됐습니다.

**해결:** `api-docs.path`를 기본값(`/v3/api-docs`)으로 유지하고, `GlobalExceptionHandler`에 `NoResourceFoundException` 전용 핸들러를 추가하여 404를 명확하게 반환하도록 수정했습니다.

### GET /api/questions에서 `sort` 파라미터 500 오류

Spring의 `Pageable` 자동 바인딩을 사용하면 Swagger에서 `sort: ["string"]` 형태의 잘못된 기본값이 전송되어 500 오류가 발생했습니다.  
또한 존재하지 않는 필드명을 정렬 기준으로 사용할 때도 에러 메시지 없이 500이 반환됐습니다.

**해결:** `Pageable` 대신 `page`, `size`, `sort`를 `@RequestParam`으로 명시적으로 받도록 변경하고, `InvalidDataAccessApiUsageException` 핸들러를 추가하여 400으로 응답하도록 처리했습니다.
