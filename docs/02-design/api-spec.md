# API Specification

본 문서는 `backend-interview-tracker` MVP의 백엔드 API 명세서입니다.

## 1. 공통 응답 포맷 (Common Response)

모든 커스텀 API의 응답은 다음과 같은 형식을 따릅니다.

### 성공 시
```json
{
  "success": true,
  "message": "성공 메시지",
  "data": { 
     // 실제 비즈니스 응답 데이터 
  }
}
```

### 실패 시 (예: Validation 실패)
```json
{
  "success": false,
  "message": "title 파라미터는 필수입니다.",
  "data": null
}
```

---

## 2. API 목록

### 2.1 질문 단건 등록 (Create Question)
- **Method:** `POST`
- **Path:** `/api/questions`
- **Description:** 새로운 인터뷰 질문을 등록합니다.
- **Request Body (JSON)**
  ```json
  {
    "title": "JPA N+1 문제가 무엇인가요?", // 필수 (NotBlank)
    "answer": "연관 관계가 설정된 엔티티를 조회할 때...", // 필수 (NotBlank)
    "source": "https://example.com/interview", // 선택
    "category": "SPRING", // 필수 (NotNull)
    "status": "UNKNOWN" // 필수 (NotNull)
  }
  ```
- **Response Data:** 생성된 Question의 ID (Long)

### 2.2 질문 단건 조회 (Get Question)
- **Method:** `GET`
- **Path:** `/api/questions/{id}`
- **Description:** 특정 질문의 상세 정보를 조회합니다.
- **Response Data:**
  ```json
  {
    "id": 1,
    "title": "JPA N+1 문제가 무엇인가요?",
    "answer": "연관 관계가 설정된 엔티티를 조회할 때...",
    "source": "https://example.com/interview",
    "category": "SPRING",
    "status": "UNKNOWN",
    "createdAt": "2024-01-01T12:00:00",
    "updatedAt": "2024-01-01T12:00:00"
  }
  ```

### 2.3 질문 목록 조회 및 검색 (Search Questions)
- **Method:** `GET`
- **Path:** `/api/questions`
- **Description:** 질문 목록을 페이징하여 조회합니다. 카테고리, 상태, 키워드로 필터링이 가능합니다.
- **Query Parameters:**
  - `keyword` (String, 선택): 제목 또는 내용에 포함된 문자열 검색
  - `category` (Enum, 선택): 카테고리 필터 (`JAVA`, `SPRING`, `DB` 등)
  - `status` (Enum, 선택): 상태 필터 (`UNKNOWN`, `UNCERTAIN`, `MASTERED`)
  - `page` (int): 페이지 번호 (기본값: 0)
  - `size` (int): 페이지 크기 (기본값: 10)
- **Response Data:** Spring Data `Page` 객체 (컨텐츠 리스트 포함)

### 2.4 질문 수정 (Update Question)
- **Method:** `PUT`
- **Path:** `/api/questions/{id}`
- **Description:** 기존 질문의 정보를 수정합니다.
- **Request Body (JSON)**
  ```json
  {
    "title": "수정된 질문 제목", // 필수
    "answer": "수정된 답변 내용", // 필수
    "source": "수정된 출처", // 선택
    "category": "JAVA", // 필수
    "status": "MASTERED" // 필수
  }
  ```
- **Response Data:** 수정된 Question의 상세 정보

### 2.5 질문 삭제 (Delete Question)
- **Method:** `DELETE`
- **Path:** `/api/questions/{id}`
- **Description:** 특정 질문을 삭제합니다.
- **Response Data:** `null` (성공 시 success: true 반환)
