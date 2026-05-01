# Architecture & Quality Review

## 1. 아키텍처 개요
- **Layered Architecture:** Controller - Service - Repository 계층 분리 원칙을 준수함.
- **DTO Separation:** 외부 API 스펙(DTO)과 내부 엔티티(Entity)를 철저히 분리함.
- **Exception Handling:** `@RestControllerAdvice`를 활용하여 비즈니스/검증 예외를 전역에서 일관성 있게 처리함.
- **Auditing:** JPA Auditing을 활용하여 `createdAt`, `updatedAt`이 자동으로 관리됨.

## 2. 리뷰 포인트
- [x] **N+1 문제 검토:** 현재 단일 엔티티(Question) 구조이므로 연관관계 매핑으로 인한 N+1 문제는 발생하지 않음. 향후 다대다(Tag) 매핑 시 Fetch Join 또는 Entity Graph 적용 필요.
- [x] **트랜잭션 경계:** `QuestionService`의 모든 비즈니스 로직에 `@Transactional`이 적용되어 있으며, 조회 전용에는 `readOnly = true` 최적화가 적용됨.
- [x] **테스트 커버리지:** Service의 비즈니스 로직 및 Controller의 Validation/응답이 Mock 객체를 통해 독립적으로 테스트됨.

## 3. K-Layer Knowledge
- Entity에 `@EntityListeners(AuditingEntityListener.class)`를 적용할 때 메인 애플리케이션에 `@EnableJpaAuditing`을 누락하지 않도록 주의해야 합니다.
- Spring Boot 환경에서 MockMvc 테스트 시 `@MockBean`을 사용하여 의존성을 쉽게 격리할 수 있습니다.
