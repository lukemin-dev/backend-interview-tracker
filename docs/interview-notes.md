# Backend Interview Tracker Interview Notes

이 문서는 `backend-interview-tracker`를 면접에서 설명하기 위한 정리입니다.

## 1. 프로젝트 한 줄 설명

백엔드 면접 질문과 답변을 카테고리, 이해도 상태, 키워드로 관리할 수 있는 Spring Boot REST API 서버입니다.

## 2. 처음 문제

- 백엔드 면접 준비를 하면서 질문, 답변, 출처, 이해도 상태가 여러 문서에 흩어지기 쉬웠습니다.
- 단순 메모장 방식으로는 "어떤 질문을 아직 모르는지", "Java/Spring/DB 중 어떤 영역이 약한지"를 빠르게 보기 어려웠습니다.
- 면접 대비 프로젝트로 설명하려면 단순 CRUD보다 API 설계, 예외 처리, 검색, 테스트를 함께 보여줄 필요가 있었습니다.

## 3. 내가 한 목표

- 질문과 답변을 REST API로 등록, 조회, 수정, 삭제할 수 있게 만들기
- `keyword`, `category`, `status` 조건으로 질문을 검색할 수 있게 만들기
- 모든 API 응답을 공통 포맷으로 통일하기
- 잘못된 요청, 존재하지 않는 데이터, 잘못된 정렬 조건을 일관된 에러 응답으로 처리하기
- Controller와 Service 계층 테스트를 작성해 기본 동작을 검증하기

## 4. 구현 과정

### 도메인과 계층 분리

- `Question` 엔티티에 질문 제목, 답변, 출처, 카테고리, 상태를 저장합니다.
- `Category`, `Status` enum으로 질문 분류와 이해도 상태를 제한했습니다.
- Controller, Service, Repository 계층을 분리해 HTTP 처리, 비즈니스 로직, 데이터 접근 책임을 나눴습니다.

관련 파일:

- `src/main/java/com/example/interviewtracker/domain/Question.java`
- `src/main/java/com/example/interviewtracker/controller/QuestionController.java`
- `src/main/java/com/example/interviewtracker/service/QuestionService.java`
- `src/main/java/com/example/interviewtracker/repository/QuestionRepository.java`

### 공통 응답 포맷

- `ApiResponse<T>`로 성공/실패 응답 구조를 통일했습니다.
- 클라이언트가 API별로 다른 응답 구조를 처리하지 않아도 되도록 `success`, `message`, `data` 형태를 유지했습니다.

관련 파일:

- `src/main/java/com/example/interviewtracker/common/ApiResponse.java`

### 검색과 필터링

- `GET /api/questions`에서 `keyword`, `category`, `status`, `page`, `size`, `sort`를 지원합니다.
- Repository에서 JPQL을 사용해 선택 조건이 없으면 전체 조회, 조건이 있으면 조합 검색이 가능하도록 구성했습니다.

관련 파일:

- `src/main/java/com/example/interviewtracker/repository/QuestionRepository.java`

### 예외 처리

- `GlobalExceptionHandler`에서 validation 실패, enum 파싱 실패, 존재하지 않는 리소스, 잘못된 정렬 필드 등을 처리합니다.
- 에러가 발생해도 응답 형식이 흐트러지지 않게 `ApiResponse.error(...)`로 반환합니다.

관련 파일:

- `src/main/java/com/example/interviewtracker/common/GlobalExceptionHandler.java`
- `src/main/java/com/example/interviewtracker/common/ResourceNotFoundException.java`

### 테스트

- `QuestionControllerTest`는 MockMvc로 API 요청/응답과 validation 실패를 확인합니다.
- `QuestionServiceTest`는 Mockito로 Repository를 격리하고 생성/조회 로직을 검증합니다.

관련 파일:

- `src/test/java/com/example/interviewtracker/controller/QuestionControllerTest.java`
- `src/test/java/com/example/interviewtracker/service/QuestionServiceTest.java`

## 5. 막혔던 점

- JPA Auditing을 사용하면 테스트 환경에서 필요한 설정이 함께 로딩되어야 해서 WebMvcTest와 충돌할 수 있습니다.
- 검색 API에서 정렬 필드를 문자열로 받기 때문에 존재하지 않는 필드가 들어올 때 예외 처리가 필요했습니다.
- enum 값이 잘못 들어오면 JSON 파싱 단계에서 실패하므로 일반 validation과 다른 방식으로 처리해야 했습니다.

## 6. 해결 방법

- JPA Auditing 설정을 분리해 테스트 계층에서 불필요한 JPA 설정 충돌을 줄였습니다.
- `PropertyReferenceException`, `InvalidDataAccessApiUsageException`, `HttpMessageNotReadableException`을 전역 예외 처리에 추가했습니다.
- 조회 전용 메서드에는 `@Transactional(readOnly = true)`를 적용해 트랜잭션 의도를 명확히 했습니다.

## 7. 검증

실행 명령:

```bash
./gradlew clean test
```

Windows에서는:

```bash
gradlew.bat clean test
```

검증 범위:

- 질문 등록 API 성공
- 질문 등록 validation 실패
- 질문 단건 조회
- Service 계층 질문 생성
- Service 계층 질문 조회
- GitHub Actions에서 `main` push와 pull request마다 테스트 실행

## 8. 면접에서 말할 포인트

### 계층 분리

> Controller는 HTTP 요청과 응답만 담당하게 하고, 실제 질문 생성/검색/수정/삭제 로직은 Service에 두었습니다. Repository는 데이터 접근만 담당하도록 분리했습니다. 이렇게 나누면 API 스펙이 바뀌어도 비즈니스 로직을 비교적 안정적으로 유지할 수 있습니다.

### DTO 분리

> 외부 요청/응답 DTO와 내부 Entity를 분리했습니다. Entity를 그대로 노출하면 DB 구조가 API 스펙에 묶이기 때문에, Request/Response DTO를 통해 외부 계약을 따로 관리했습니다.

### 전역 예외 처리

> API마다 try-catch를 넣는 대신 `@RestControllerAdvice`로 validation, not found, enum 파싱, 잘못된 정렬 조건을 한 곳에서 처리했습니다. 덕분에 실패 응답도 공통 포맷으로 유지할 수 있었습니다.

### 검색 API

> 처음에는 단순 목록 조회만으로 충분해 보였지만, 면접 질문은 카테고리와 이해도 상태로 자주 필터링해야 합니다. 그래서 keyword, category, status를 조합해 검색할 수 있게 만들었습니다.

### 테스트

> Controller 테스트는 HTTP 요청/응답과 validation을 확인하고, Service 테스트는 Repository를 Mock으로 격리해 비즈니스 로직을 확인했습니다. 계층별로 테스트 목적을 나눴습니다.

## 9. 다시 한다면 개선할 점

- 검색 조건이 더 많아지면 JPQL 문자열 대신 QueryDSL 또는 Specification을 도입할 수 있습니다.
- MySQL과 Docker Compose를 붙여 실제 운영 DB와 비슷한 환경에서 테스트할 수 있습니다.
- Spring Security와 JWT를 추가해 사용자별 질문 관리를 지원할 수 있습니다.
- Controller 테스트에 검색/수정/삭제 케이스를 더 추가할 수 있습니다.
- Testcontainers를 사용해 DB 통합 테스트 신뢰도를 높일 수 있습니다.

## 10. 30초 답변 예시

> 백엔드 면접 질문을 카테고리와 이해도 상태별로 관리하기 위한 Spring Boot REST API를 만들었습니다. 단순 CRUD에 그치지 않고 공통 응답 포맷, 전역 예외 처리, keyword/category/status 조합 검색, 페이지네이션과 정렬을 구현했습니다. Controller, Service, Repository 계층을 분리했고, MockMvc와 Mockito 테스트로 API 응답과 서비스 로직을 검증했습니다. 이 프로젝트는 Spring 기반 API 설계와 테스트, 예외 처리 기본기를 설명하기 좋은 프로젝트입니다.
