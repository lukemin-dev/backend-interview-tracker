# Database Schema Design

본 문서는 `backend-interview-tracker` MVP에서 사용하는 핵심 엔티티 구조를 정의합니다.

## Entity: Question

인터뷰 질문과 관련된 주요 정보를 저장하는 엔티티입니다. 복잡한 연관관계 없이 단일 테이블로 관리합니다.

| 필드명 (Field) | 타입 (Type) | 제약 조건 (Constraints) | 설명 (Description) |
|---|---|---|---|
| `id` | `Long` / `BIGINT` | Primary Key, Auto Increment | 질문 고유 식별자 |
| `title` | `String` / `VARCHAR(255)` | Not Null | 질문 제목 및 핵심 내용 |
| `answer` | `String` / `TEXT` | Not Null | 질문에 대한 모범 답안 또는 사용자의 정리 내용 |
| `source` | `String` / `VARCHAR(255)` | Nullable | 질문 출처 (면접 기업명, 블로그 링크, 책 등) |
| `category` | `Enum` / `VARCHAR(50)` | Not Null | 질문이 속하는 카테고리 (`JAVA`, `SPRING`, `DB`, `NETWORK` 등) |
| `status` | `Enum` / `VARCHAR(50)` | Not Null | 사용자의 이해도 상태 (`UNKNOWN`, `UNCERTAIN`, `MASTERED`) |
| `createdAt` | `LocalDateTime` / `DATETIME` | Not Null, Updatable=false | 데이터 최초 등록 일시 |
| `updatedAt` | `LocalDateTime` / `DATETIME` | Not Null | 데이터 최종 수정 일시 |

## Enums

### Category
질문의 대분류를 관리합니다.
- `JAVA`: Java 언어 관련
- `SPRING`: Spring Framework 관련
- `DB`: 데이터베이스 관련 (RDBMS, NoSQL 등)
- `NETWORK`: 네트워크 및 인프라 관련
- `OS`: 운영체제 관련
- `ETC`: 기타 기술

### Status
사용자의 현재 이해도 상태를 관리합니다.
- `UNKNOWN`: 질문의 의도나 답변을 전혀 모르는 상태
- `UNCERTAIN`: 일부 알고 있으나 명확하게 설명하기 어려운 상태
- `MASTERED`: 완벽하게 이해하고 구두로 설명 가능한 상태
