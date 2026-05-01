# Terminology Dictionary

본 문서는 `backend-interview-tracker` 프로젝트 내에서 공통으로 사용되는 용어의 정의를 명확히 하여 개발간 혼선을 방지합니다.

| 용어 (Term) | 설명 (Description) | 비고 |
|---|---|---|
| **Question** | 면접이나 학습 과정에서 마주친 기술적 질문 데이터 단위를 의미합니다. | 도메인 핵심 엔티티 |
| **Answer** | Question에 대해 작성자가 직접 정리한 기술적 답변 및 풀이 내용입니다. | |
| **Category** | 특정 기술 스택이나 주제로 Question을 묶는 분류 단위입니다. (예: `JAVA`, `SPRING`) | Enum 타입으로 구현 |
| **Status** | Question에 대한 사용자의 현재 이해도를 나타냅니다. | Enum 타입으로 구현 |
| **Source** | 해당 Question을 접하게 된 출처(면접장, 특정 회사, 링크, 도서 등)를 기록하는 항목입니다. | |
| **UNKNOWN** | Status 중 하나로, '전혀 모르는 상태'를 의미합니다. | |
| **UNCERTAIN** | Status 중 하나로, '애매하게 아는 상태'를 의미합니다. | |
| **MASTERED** | Status 중 하나로, '완벽하게 이해한 상태'를 의미합니다. | |
