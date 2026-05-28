# Backend Interview Tracker

[![CI](https://github.com/lukemin-dev/backend-interview-tracker/actions/workflows/ci.yml/badge.svg)](https://github.com/lukemin-dev/backend-interview-tracker/actions/workflows/ci.yml)

**[한국어](./README.md)** · [English](./README.en.md) · [日本語](./README.ja.md)

백엔드 면접 준비를 위해 질문과 답안을 카테고리별로 정리하고 이해도를 관리할 수 있는 Spring Boot REST API 서버입니다. CRUD API, 예외 처리, 동적 검색, 테스트 코드를 직접 구현하며 백엔드 기본기를 확인하는 것을 목표로 만들었습니다.

## 기술 스택

| 분류 | 사용 기술 |
|---|---|
| 언어 | Java 17 |
| 프레임워크 | Spring Boot 3.2.4 |
| 데이터베이스 | H2 In-memory |
| ORM | Spring Data JPA / Hibernate |
| 빌드 | Gradle 8.7 |
| 테스트 | JUnit 5, Mockito, MockMvc |
| 문서화 | Springdoc OpenAPI / Swagger UI |

## API

| Method | URL | 설명 |
|---|---|---|
| POST | `/api/questions` | 질문 등록 |
| GET | `/api/questions/{id}` | 질문 단건 조회 |
| GET | `/api/questions` | 질문 목록 조회, 검색, 필터, 정렬 |
| PUT | `/api/questions/{id}` | 질문 수정 |
| DELETE | `/api/questions/{id}` | 질문 삭제 |

`GET /api/questions`는 `keyword`, `category`, `status`, `page`, `size`, `sort` 쿼리 파라미터를 지원합니다.

## 로컬 실행

```bash
git clone https://github.com/lukemin-dev/backend-interview-tracker.git
cd backend-interview-tracker
./gradlew clean test
./gradlew bootRun --args='--spring.profiles.active=local'
```

Windows에서는 `./gradlew` 대신 `gradlew.bat`을 사용합니다.

## 접속 경로

| 서비스 | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| API Docs | `http://localhost:8080/v3/api-docs` |
| H2 Console | `http://localhost:8080/h2-console` |

H2 콘솔과 SQL 로그는 `local` profile에서만 켜집니다. 기본 `application.yml`은 로컬 진단 기능을 꺼 둔 상태로 유지합니다.

## 요청 예시

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

```bash
curl "http://localhost:8080/api/questions?page=0&size=10&sort=createdAt,desc"
curl "http://localhost:8080/api/questions?category=JAVA&status=UNCERTAIN"
curl "http://localhost:8080/api/questions?keyword=JVM"
```

## 구현 포인트

- 공통 응답 포맷 `ApiResponse<T>`로 API 응답 구조 통일
- `GlobalExceptionHandler`를 통한 validation, enum, not found, sort 오류 처리
- `keyword`, `category`, `status` 조합 검색을 위한 JPQL 기반 동적 검색
- `@WebMvcTest`와 JPA Auditing 충돌을 피하기 위한 설정 분리
- 기본 설정과 로컬 진단 설정을 분리해 운영형 설정 감각을 드러냄

## 검증

```bash
./gradlew clean test
```

GitHub Actions는 `main` push와 pull request에서 동일한 테스트를 실행합니다.

## 향후 개선

1. MySQL 8.0과 Docker Compose 적용
2. QueryDSL 기반 검색 리팩터링
3. Spring Security + JWT 인증/인가 추가
4. GitHub Actions 기반 배포 파이프라인 확장
5. 클라우드 배포 환경 분리
