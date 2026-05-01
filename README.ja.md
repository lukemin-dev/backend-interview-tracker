# Backend Interview Tracker

[한국어](./README.md) · [English](./README.en.md) · **[日本語](./README.ja.md)**

バックエンドエンジニアの面接対策として、質問と模範解答をカテゴリ別に管理し、理解度を記録・復習できるREST APIサーバーです。  
Java、Spring Boot、JPAを使ったCRUD API設計・例外処理・テストコード作成を通じて、バックエンドの基礎を実践的に習得することを目的として開発しました。

---

## 🛠️ 技術スタック

| カテゴリ | 使用技術 |
|---|---|
| 言語 | Java 17 |
| フレームワーク | Spring Boot 3.2.4 |
| データベース | H2 (インメモリ) |
| ORM | Spring Data JPA / Hibernate |
| ビルド | Gradle 8.7 |
| テスト | JUnit 5, Mockito, MockMvc |
| ドキュメント | Springdoc OpenAPI (Swagger UI) |
| その他 | Lombok, Spring Validation |

---

## 📌 API 一覧

| メソッド | URL | 説明 |
|---|---|---|
| POST | `/api/questions` | 質問を登録する |
| GET | `/api/questions/{id}` | 質問を1件取得する |
| GET | `/api/questions` | 質問一覧取得（ページング・フィルター・ソート） |
| PUT | `/api/questions/{id}` | 質問を更新する |
| DELETE | `/api/questions/{id}` | 質問を削除する |

### クエリパラメータ (GET /api/questions)

| パラメータ | 型 | デフォルト | 説明 |
|---|---|---|---|
| `keyword` | String | - | タイトルまたは解答のキーワード検索 |
| `category` | Enum | - | `JAVA`, `SPRING`, `DB`, `NETWORK`, `OS`, `ETC` |
| `status` | Enum | - | `UNKNOWN`, `UNCERTAIN`, `MASTERED` |
| `page` | int | `0` | ページ番号 |
| `size` | int | `10` | ページサイズ |
| `sort` | String | `createdAt,desc` | ソート項目と方向（`フィールド名,asc\|desc`） |

---

## ⚙️ ローカル実行手順

### 1. リポジトリのクローン

```bash
git clone https://github.com/your-username/backend-interview-tracker.git
cd backend-interview-tracker
```

### 2. テストの実行

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

- `QuestionServiceTest` — Mockitoを使ったサービス層のビジネスロジックテスト
- `QuestionControllerTest` — MockMvcを使ったAPIバリデーション・例外処理テスト

### 3. サーバーの起動

```bash
# Windows
.\gradlew.bat bootRun

# macOS / Linux
./gradlew bootRun
```

起動に成功すると、コンソールに以下のメッセージが表示されます。

```text
Tomcat started on port 8080 (http) with context path ''
```

### 4. アクセスURL

| サービス | URL |
|---|---|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| API Docs (JSON) | `http://localhost:8080/v3/api-docs` |
| H2コンソール | `http://localhost:8080/h2-console` |

> H2接続情報: JDBC URL `jdbc:h2:mem:interviewdb` / ユーザー名 `sa` / パスワードなし

---

## 📝 APIリクエスト例

### 質問の登録 (POST /api/questions)

```bash
curl -X POST http://localhost:8080/api/questions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "JVMとは何ですか？",
    "answer": "JVMはJava Virtual Machineの略で、Javaバイトコードを実行する仮想マシンです。プラットフォーム非依存性を実現します。",
    "source": "backend-interview-question GitHub",
    "category": "JAVA",
    "status": "UNCERTAIN"
  }'
```

**レスポンス**

```json
{
  "success": true,
  "message": "성공적으로 처리되었습니다.",
  "data": 1
}
```

### 質問一覧の取得 (GET /api/questions)

```bash
# デフォルト（最新10件）
curl "http://localhost:8080/api/questions"

# ページング・ソート指定
curl "http://localhost:8080/api/questions?page=0&size=10&sort=createdAt,desc"

# カテゴリ・理解度でフィルタリング
curl "http://localhost:8080/api/questions?category=JAVA&status=UNCERTAIN"

# キーワード検索
curl "http://localhost:8080/api/questions?keyword=JVM"
```

### 質問の更新 (PUT /api/questions/{id})

```bash
curl -X PUT http://localhost:8080/api/questions/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "JVMとは何ですか？",
    "answer": "JVMはJavaバイトコードを実行する仮想マシンで、プラットフォーム非依存性を保証します。",
    "source": "Java公式ドキュメント",
    "category": "JAVA",
    "status": "MASTERED"
  }'
```

### 質問の削除 (DELETE /api/questions/{id})

```bash
curl -X DELETE http://localhost:8080/api/questions/1
```

---

## 🔍 実装のポイント

- **統一レスポンス形式 (`ApiResponse<T>`):** 全エンドポイントが `success`、`message`、`data` フィールドを持つ一貫したJSONを返します。
- **グローバル例外ハンドラー:** バリデーションエラー・不正なEnum値・存在しないソートフィールドなど、様々なエラーを明確な400/404メッセージで返します。500エラーをそのままクライアントに露出させません。
- **JPQLによる動的検索:** `keyword`、`category`、`status` を条件に応じて組み合わせるクエリを直接記述しました。
- **JPA Auditing分離:** `@WebMvcTest` 環境での `@EnableJpaAuditing` の競合を防ぐため、`JpaConfig` を別クラスに分離しました。

---

## 🔮 今後の改善計画

1. **MySQLへの移行** — H2インメモリDBをMySQL 8.0に切り替える
2. **Docker Compose対応** — アプリとDBをコンテナ化して環境非依存で実行できるようにする
3. **QueryDSL導入** — 複雑な検索条件をタイプセーフに処理する
4. **Spring Security + JWT** — ユーザー認証・認可機能の追加、個人別質問管理
5. **CI/CD・デプロイ** — GitHub ActionsでCI/CDパイプラインを構築し、AWS EC2またはRailwayにデプロイ

---

## ⚠️ トラブルシューティング

### Swagger `/v3/api-docs` が500エラーを返す

`application.yml` の `springdoc.api-docs.path` を `/api-docs` に変更したことが原因でした。Swagger UIのフロントエンドはデフォルトで `/v3/api-docs` を固定で呼び出すため、パスが変わると `NoResourceFoundException` が発生します。さらに `GlobalExceptionHandler` の最上位 `Exception` ハンドラーが500にマッピングしていたため、本来の404が隠蔽されていました。

**対処法:** `api-docs.path` をデフォルト値（`/v3/api-docs`）に戻し、`GlobalExceptionHandler` に `NoResourceFoundException` 専用ハンドラーを追加して404を明示的に返すよう修正しました。

### GET /api/questions で `sort` パラメータ指定時に500エラー

Springの `Pageable` 自動バインディングを使用すると、SwaggerがデフォルトでSort値に `"string"` を送信し500エラーが発生しました。また、存在しないフィールド名をソート条件に使用した際も、エラーメッセージなしで500が返されていました。

**対処法:** `Pageable` の代わりに `page`、`size`、`sort` を `@RequestParam` で明示的に受け取るよう変更し、`InvalidDataAccessApiUsageException` ハンドラーを追加して400を返すよう対応しました。
