#
# Server Module
#

This document explains how the `server` module is structured, which libraries it uses,
how configuration works, how HTTP requests/responses are shaped, and how to extend the
server safely (DTOs, routes, headers, logic modules, repositories).
---

## 1) Server module structure

Path: `server/src/main/kotlin/com/antik/wallet/server`

High‑level layout (modular monolith + ports/adapters):

- `bootstrap/`
  - `ApplicationModule.kt` – Ktor plugin setup + routing wiring.
  - `Routing.kt` – collects and registers all module routes.
  - `Modules.kt` – central dependency wiring (ports -> infra adapters).
  - `AppConfig.kt` – configuration model + env loader.
- `shared/`
  - `errors/` – `ApiError`, exception mapping (`StatusPages`).
  - `validation/` – validation helpers.
  - `tracing/` – request id handling (CallId).
  - `health/` – health endpoints (live/ready).
- `modules/<feature>/`
  - `domain/` – pure domain models and rules.
  - `application/` – use cases and orchestration.
  - `application/ports/` – interfaces (repositories, external clients).
  - `infra/` – implementation of ports (DB, HTTP, cache).
  - `api/` – Ktor routes + DTO mappers.

Rule of dependencies:
- `domain` has **no** dependencies on Ktor, DB, HTTP, or infra.
- `application` depends on `domain` + `ports` only.
- `infra` implements ports and depends on external libs.
- `api` depends on `application` and DTO mappers.

---

## 2) Libraries used (server)

From `server/build.gradle.kts` and `gradle/libs.versions.toml`:
- Ktor server core, Netty
- Ktor ContentNegotiation (JSON)
- Ktor CallId (request id)
- Ktor CallLogging
- Ktor StatusPages (error mapping)
- Ktor Compression (gzip)
- Kotlinx Serialization (JSON)
- Logback (logging)
- Test: Ktor test host, Kotlin test, Ktor client content negotiation

Notes:
- JSON is handled by Kotlinx Serialization.
- Errors are normalized to `ApiError`.
- Request ID is enforced by CallId.

---

## 3) Configuration

Config model: `server/bootstrap/AppConfig.kt`

Environment variables (current):
- `SERVER_HOST` (string)
- `SERVER_PORT` (int)
- `SERVER_PUBLIC_URL` (string, optional)

The values are read in `AppConfigLoader.fromEnv()`.

Production run task:
```
./gradlew :server:runProd -PprodHost=... -PprodPort=... -PprodPublicUrl=...
```
These Gradle properties are required for `runProd`. Missing ones cause a build error.

---

## 4) Request/response format

### 4.1 Successful responses
- Default: JSON responses using Kotlinx Serialization.
- Use DTO classes from the `dto` module (shared with clients).
- Ktor `ContentNegotiation` handles serialization automatically.

### 4.2 Error responses (standardized)
All errors must map to `ApiError`:
```json
{
  "code": "validation_error",
  "message": "Human readable message",
  "details": "optional details",
  "requestId": "uuid"
}
```

Exception mapping:
- `ValidationException` → 400
- `NotFoundException` → 404
- `ConflictException` → 409
- unknown error → 500 (`unexpected_error`)

### 4.3 Request ID header
We use a request-id header for tracing:
- Header key: `ApiHeaders.REQUEST_ID` (value `X-Request-Id`)
- If client doesn’t send it, server generates a UUID.
- The same header is returned in responses.

---

## 5) How to add new DTO models

DTOs live in the `dto` module.

Steps:
1) Create `@Serializable` data class in `dto/src/commonMain/...`.
2) Keep DTOs **data-only** (no server logic).
3) Use DTOs only in `api` layer on the server.

Example:
```kotlin
@Serializable
data class ItemDto(
    val id: Int,
    val name: String
)
```

---

## 6) How to add new API routes

Routes belong to the module’s `api/` package.

Steps:
1) Create `<Feature>Routes.kt` in `modules/<feature>/api`.
2) Map DTO ↔ domain using dedicated mappers.
3) Use use‑cases from `application/`.
4) Create a dedicated test file for the new routes (see section 11).
5) Register in `bootstrap/Routing.kt`.

Example:
```kotlin
fun Route.configureFeatureRoutes(service: FeatureUseCase) {
    get("/api/feature") { ... }
}
```

---

## 7) How to add HTTP headers

Headers should be centralized in `api` module:
- Add constants to `api/ApiHeaders.kt`.
- Use them in server and client.

Example:
```kotlin
object ApiHeaders {
    const val REQUEST_ID = "X-Request-Id"
}
```

---

## 8) How to add a new logic module

Steps:
1) Create `modules/<feature>/domain` with pure models.
2) Create `modules/<feature>/application` for use cases.
3) Define ports in `modules/<feature>/application/ports`.
4) Implement ports in `modules/<feature>/infra`.
5) Create routes + DTO mappers in `modules/<feature>/api`.
6) Wire dependencies in `bootstrap/Modules.kt`.
7) Register routes in `bootstrap/Routing.kt`.

The goal is to keep domain and application independent of infrastructure.

---

## 9) How to add repositories

Repository is a **port** in `application/ports`, and implementation in `infra`.

Steps:
1) Define interface in `ports/`.
2) Implement it in `infra/`.
3) Provide implementation in `ModuleDependencies`.

Example:
```kotlin
interface FeatureRepository {
    fun findById(id: Int): Feature
}

class FeatureRepositoryDb(...) : FeatureRepository { ... }
```

---

## 10) Relation with `api` and `dto` modules

### `api` module
Contains shared API contracts:
- route constants (e.g., base paths)
- header constants
- optional shared config objects

Use it when:
- server and client must agree on the same route/header names.

### `dto` module
Contains shared DTO models:
- request/response models
- shared serializable types

Use it when:
- client and server exchange JSON payloads.

Server should **not** keep DTOs in its own module unless they are server-only.

---

## 11) Testing guidelines

Tests are in `server/src/test`.
Use `io.ktor.server.testing.testApplication`:
- configure `Application.module(...)`
- create Ktor client with JSON plugin
- assert HTTP status and DTO bodies

Do not use real external services in tests; rely on in‑memory or fake ports.

---

## 12) Non‑requirements (explicit)

Current endpoints are sample‑level only and will be removed.
Avoid referencing sample entity names in new architecture docs or code.

---

End of document.
