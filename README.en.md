# Backend Interview Tracker

[한국어](./README.md) · **[English](./README.en.md)** · [日本語](./README.ja.md)

A REST API server for managing backend interview questions — organize Q&A by category, track your understanding, and review efficiently.  
Built with Java, Spring Boot, and JPA to practice core backend skills: CRUD API design, exception handling, and test coverage.

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.4 |
| Database | H2 (In-memory) |
| ORM | Spring Data JPA / Hibernate |
| Build | Gradle 8.7 |
| Testing | JUnit 5, Mockito, MockMvc |
| Documentation | Springdoc OpenAPI (Swagger UI) |
| Etc | Lombok, Spring Validation |

---

## 📌 API Reference

| Method | URL | Description |
|---|---|---|
| POST | `/api/questions` | Create a question |
| GET | `/api/questions/{id}` | Get a single question |
| GET | `/api/questions` | List questions (paging, filter, sort) |
| PUT | `/api/questions/{id}` | Update a question |
| DELETE | `/api/questions/{id}` | Delete a question |

### Query Parameters (GET /api/questions)

| Parameter | Type | Default | Description |
|---|---|---|---|
| `keyword` | String | - | Search in title or answer |
| `category` | Enum | - | `JAVA`, `SPRING`, `DB`, `NETWORK`, `OS`, `ETC` |
| `status` | Enum | - | `UNKNOWN`, `UNCERTAIN`, `MASTERED` |
| `page` | int | `0` | Page number |
| `size` | int | `10` | Page size |
| `sort` | String | `createdAt,desc` | Sort field and direction (`field,asc\|desc`) |

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/backend-interview-tracker.git
cd backend-interview-tracker
```

### 2. Run Tests

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

- `QuestionServiceTest` — Business logic with Mockito
- `QuestionControllerTest` — API validation and exception handling with MockMvc

### 3. Run the Server

```bash
# Windows
.\gradlew.bat bootRun

# macOS / Linux
./gradlew bootRun
```

When the server starts successfully, you'll see:

```text
Tomcat started on port 8080 (http) with context path ''
```

### 4. Access URLs

| Service | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| API Docs (JSON) | `http://localhost:8080/v3/api-docs` |
| H2 Console | `http://localhost:8080/h2-console` |

> H2 credentials: JDBC URL `jdbc:h2:mem:interviewdb` / Username `sa` / No password

---

## 📝 API Examples

### Create a Question (POST /api/questions)

```bash
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "What is the JVM?",
    "answer": "JVM stands for Java Virtual Machine. It executes Java bytecode and provides platform independence.",
    "source": "backend-interview-question GitHub",
    "category": "JAVA",
    "status": "UNCERTAIN"
  }'
```

**Response**

```json
{
  "success": true,
  "message": "성공적으로 처리되었습니다.",
  "data": 1
}
```

### List Questions (GET /api/questions)

```bash
# Default (latest 10)
curl "http://localhost:8080/api/questions"

# With paging and sort
curl "http://localhost:8080/api/questions?page=0&size=10&sort=createdAt,desc"

# Filter by category and status
curl "http://localhost:8080/api/questions?category=JAVA&status=UNCERTAIN"

# Keyword search
curl "http://localhost:8080/api/questions?keyword=JVM"
```

### Update a Question (PUT /api/questions/{id})

```bash
curl -X PUT http://localhost:8080/api/questions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "What is the JVM?",
    "answer": "JVM is a virtual machine that executes Java bytecode and ensures platform independence.",
    "source": "Official Java Documentation",
    "category": "JAVA",
    "status": "MASTERED"
  }'
```

### Delete a Question (DELETE /api/questions/{id})

```bash
curl -X DELETE http://localhost:8080/api/questions/1
```

---

## 🔍 Implementation Highlights

- **Unified Response Format (`ApiResponse<T>`):** All endpoints return a consistent JSON structure with `success`, `message`, and `data` fields.
- **Global Exception Handler:** Handles validation errors, invalid Enum values, and unknown sort fields with clear 400/404 messages — no raw 500s exposed to the client.
- **Dynamic JPQL Search:** `keyword`, `category`, and `status` are conditionally combined in a single JPQL query.
- **JPA Auditing Isolation:** `@EnableJpaAuditing` was extracted to a separate `JpaConfig` class to prevent conflicts with `@WebMvcTest` in controller tests.

---

## 🔮 Planned Improvements

1. **MySQL Migration** — Replace H2 In-memory DB with MySQL 8.0
2. **Docker Compose** — Containerize the app and DB for environment-independent deployment
3. **QueryDSL** — Type-safe dynamic queries for complex filtering
4. **Spring Security + JWT** — User authentication and per-user question management
5. **CI/CD & Deployment** — GitHub Actions pipeline with deployment to AWS EC2 or Railway

---

## ⚠️ Troubleshooting

### Swagger `/v3/api-docs` returns 500

This happened because `springdoc.api-docs.path` in `application.yml` was overridden to `/api-docs`. The Swagger UI frontend always calls `/v3/api-docs` by default, causing a `NoResourceFoundException`. Since the generic `Exception` handler in `GlobalExceptionHandler` was mapping all exceptions to 500, the actual 404 was hidden.

**Fix:** Restored `api-docs.path` to `/v3/api-docs` and added a dedicated `NoResourceFoundException` handler returning 404.

### GET /api/questions returns 500 with `sort` parameter

Using Spring's `Pageable` auto-binding caused Swagger to send `sort=string` as a default value, resulting in a 500 error. Unknown sort field names also produced unhandled exceptions.

**Fix:** Replaced `Pageable` with explicit `@RequestParam` (`page`, `size`, `sort`) and added an `InvalidDataAccessApiUsageException` handler to return 400 instead.
