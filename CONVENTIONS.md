# Development Conventions

본 문서는 `backend-interview-tracker` 개발 시 지켜야 할 주요 코드 컨벤션을 정의합니다.

## 패키지 구조 (Layered Architecture)
기능 중심이 아닌 계층형(Layered) 구조를 기본으로 사용합니다.

- `controller`: API 요청/응답을 처리하는 계층 (DTO 변환, Validation)
- `service`: 비즈니스 로직을 처리하는 계층 (트랜잭션 관리)
- `repository`: 데이터베이스 접근 계층 (Spring Data JPA)
- `domain`: JPA 엔티티(Entity) 및 Enum 클래스
- `dto`: API 요청(Request) 및 응답(Response) 객체
- `common`: 전역 예외 처리(Global Exception Handler), 공통 응답 포맷(Response Wrapper) 등

## 네이밍 규칙 (Naming Convention)
- **클래스명:** `PascalCase` (예: `QuestionService`)
- **메서드/변수명:** `camelCase` (예: `findQuestionById`)
- **DB 컬럼명:** `snake_case` (JPA 디폴트 PhysicalNamingStrategy 사용)
- **Enum 값:** `UPPER_SNAKE_CASE` (예: `TO_REVIEW`, `MASTERED`)

## API 응답 포맷 (Standard Response)
모든 커스텀 API 응답은 `ApiResponse<T>` 래퍼 클래스를 통해 감싸서 반환합니다.

```json
{
  "success": true,
  "message": "성공 메시지 또는 에러 메시지",
  "data": { ... } // 에러 발생 시 null 또는 에러 디테일
}
```

## 예외 처리 (Exception Handling)
- 비즈니스 로직 내에서는 커스텀 런타임 예외(예: `CustomException`)를 발생시킵니다.
- `@RestControllerAdvice`와 `@ExceptionHandler`를 활용해 예외를 전역에서 잡아 일관된 에러 응답을 반환합니다.

## 유효성 검사 (Validation)
- Request DTO에 `@NotBlank`, `@NotNull` 등의 제약 조건을 설정합니다.
- Controller 메서드 파라미터에 `@Valid`를 명시하여 요청을 검증합니다.
- 유효성 검증 실패 시 발생하는 `MethodArgumentNotValidException`을 `GlobalExceptionHandler`에서 잡아 클라이언트에게 명확한 에러 메시지를 전달합니다.
