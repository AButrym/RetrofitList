# Retrofit3 User Manager — Specification

**Version:** 1.1
**Date:** 2025-11-06
**Author:** Олександр Бутрим
**Validated by:** ChatGPT

---

## 1. Project Overview

**Retrofit3 User Manager** — навчальний Android-додаток, створений для демонстрації роботи з REST API за допомогою **Retrofit 3.0.0**, **Kotlinx.serialization**, **Hilt DI**, **Jetpack Compose**, та **Landscapist (Coil3)**.
Програма отримує, відображає, редагує, створює та видаляє користувачів з API, розгорнутого на **MockAPI.io**.

---

## 2. API Specification

**Base URL:**

```
https://690c993da6d92d83e84e69f9.mockapi.io/api/v1/
```

### Endpoint: `/user`

**Schema Example:**

```json
[
  {
    "id": "1",
    "name": "Jasmine Effertz",
    "avatar": "https://avatars.githubusercontent.com/u/84918628",
    "createdAt": "2025-11-06T07:20:05.017Z"
  }
]
```

### HTTP Methods

| Operation     | Method   | Endpoint     | Description                               |
| ------------- | -------- | ------------ | ----------------------------------------- |
| Get all users | `GET`    | `/user`      | Returns all users                         |
| Create user   | `POST`   | `/user`      | Creates new user                          |
| Update user   | `PUT`    | `/user/{id}` | Updates existing user (partial supported) |
| Delete user   | `DELETE` | `/user/{id}` | Deletes existing user                     |

---

## 3. Data Model

### Kotlin Data Class

```kotlin
@Serializable
data class User(
    val id: String,
    val name: String,
    val avatar: String,
    val createdAt: String
)
```

Serialization uses `kotlinx.serialization`.

---

## 4. Functional Requirements

### 4.1 Main Screen

* Displays all users sorted **by `id` descending**.
* Each user is displayed as a **card** inside a `LazyColumn`:

    * Avatar image (via Landscapist-Coil3)
    * User name
    * Created date formatted as `dd-MM-yyyy`
    * `Edit` and `Delete` `IconButton`s
* `Create` button at top opens Create screen.

**Delete flow:**

* On delete press → send `DELETE /user/{id}`
* On success → remove user card from list
* On failure → show error `Snackbar`

---

### 4.2 Edit Screen

* Displays selected user's:

    * Avatar (non-editable)
    * Text field for `name`
    * Buttons: `Submit`, `Cancel`
* On `Submit`:

    * Send `PUT /user/{id}` with `{ "name": "<new_name>" }`
    * Return to main screen on success

---

### 4.3 Create Screen

* Displays default avatar:

  ```
  https://avatars.githubusercontent.com/u/9919?s=400&v=4
  ```
* Name input field (empty)
* Buttons: `Submit`, `Cancel`
* On `Submit`:

    * Send `POST /user` with body:

      ```json
      {
        "name": "<entered_name>",
        "avatar": "https://avatars.githubusercontent.com/u/9919?s=400&v=4"
      }
      ```
    * Return to main screen on success

---

## 5. Technical Stack

| Layer             | Technology                  |
| ----------------- | --------------------------- |
| **Language**      | Kotlin 1.9.24               |
| **UI**            | Jetpack Compose (Material3) |
| **HTTP**          | Retrofit 3.0.0              |
| **Serialization** | kotlinx.serialization 1.7.3 |
| **Image Loading** | Landscapist-Coil3 2.3.7     |
| **DI**            | Hilt 2.52                   |
| **Build System**  | Gradle 8.7 + AGP 8.5.2      |
| **Min SDK**       | 24                          |
| **Target SDK**    | 34                          |

---

## 6. Architecture

**Pattern:** MVVM (Model–View–ViewModel)

**Components:**

* `UserRepository`: provides CRUD methods (Retrofit interface)
* `UserViewModel`: manages UI state, coroutine scope, and API calls
* `UserListScreen`, `UserEditScreen`, `UserCreateScreen`: Compose UIs
* `NavHost`: controls navigation between screens
* `Hilt`: provides DI for `Retrofit`, `Repository`, and `ViewModel`s

---

## 7. Retrofit Configuration

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit = Retrofit.Builder()
        .baseUrl("https://690c993da6d92d83e84e69f9.mockapi.io/api/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
}
```

---

## 8. UI Behavior Summary

| Screen | Action     | HTTP Method         | Navigation   |
| ------ | ---------- | ------------------- | ------------ |
| Main   | Tap Delete | `DELETE /user/{id}` | Stay on Main |
| Main   | Tap Edit   | —                   | Go to Edit   |
| Edit   | Submit     | `PUT /user/{id}`    | Back to Main |
| Main   | Tap Create | —                   | Go to Create |
| Create | Submit     | `POST /user`        | Back to Main |

---

## 9. Acceptance Criteria

* App builds and runs without Gradle errors.
* Retrofit requests succeed with MockAPI.io.
* Lists are reactive and reflect CRUD operations.
* Edit and Create screens navigate correctly.
* UI follows Material3 guidelines.
* No crashes on empty or slow network responses.

---

## 10. Future Extensions

* Add local caching (Room or DataStore).
* Implement pagination and pull-to-refresh.
* Integrate error handling via sealed `UiState`.
* Add test coverage with `MockWebServer`.

---
