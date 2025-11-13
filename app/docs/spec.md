# Retrofit3 User Manager — Specification

**Version:** 1.2
**Date:** 2025-11-13
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

| Layer             | Technology                                   |
| ----------------- | -------------------------------------------- |
| **Language**      | Kotlin 2.2.21                                |
| **UI**            | Jetpack Compose (Material3 via BOM)          |
| **HTTP**          | Retrofit 3.0.0                               |
| **Serialization** | kotlinx-serialization-json 1.9.0             |
| **Image Loading** | Landscapist-Coil3 2.6.1                      |
| **DI**            | Hilt 2.57.2                                  |
| **Build System**  | Gradle Wrapper + AGP 8.13.1                  |
| **Compile SDK**   | 36                                           |
| **Min SDK**       | 26                                           |
| **Target SDK**    | 36                                           |
| **JVM Target**    | 11                                           |

### 5.1. Dependencies (with exact versions)

Runtime/UI:
- androidx.core:core-ktx 1.17.0
- androidx.activity:activity-compose 1.11.0
- androidx.lifecycle:lifecycle-runtime-ktx 2.9.4
- androidx.compose:compose-bom 2025.11.00
  - androidx.compose.ui:ui (via BOM)
  - androidx.compose.ui:ui-graphics (via BOM)
  - androidx.compose.ui:ui-tooling-preview (via BOM)
  - androidx.compose.material3:material3 (via BOM)
  - androidx.compose.animation:animation (via BOM)
  - androidx.compose.material:material-icons-extended 1.7.8

Navigation:
- androidx.navigation:navigation-compose 2.9.6
- androidx.navigation3:navigation3-runtime 1.0.0-rc01
- androidx.navigation3:navigation3-ui 1.0.0-rc01
- androidx.lifecycle:lifecycle-viewmodel-navigation3 2.10.0-rc01

Networking/Serialization:
- com.squareup.retrofit2:retrofit 3.0.0
- org.jetbrains.kotlinx:kotlinx-serialization-json 1.9.0
- com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter 1.0.0
- com.squareup.okhttp3:logging-interceptor 5.3.0

Dependency Injection:
- com.google.dagger:hilt-android 2.57.2
- com.google.dagger:hilt-compiler 2.57.2 (kapt)
- androidx.hilt:hilt-navigation-compose 1.3.0

Images:
- com.github.skydoves:landscapist-coil3 2.6.1

Testing/Tooling:
- junit:junit 4.13.2
- androidx.test.ext:junit 1.3.0
- androidx.test.espresso:espresso-core 3.7.0
- androidx.compose.ui:ui-test-junit4 (via BOM)
- androidx.compose.ui:ui-tooling (debug; via BOM)
- androidx.compose.ui:ui-test-manifest (debug)

Plugins:
- com.android.application 8.13.1
- org.jetbrains.kotlin.android 2.2.21
- org.jetbrains.kotlin.plugin.compose 2.2.21
- org.jetbrains.kotlin.plugin.serialization 2.2.21
- com.google.dagger.hilt.android 2.57.2

---

## 6. Architecture

**Pattern:** MVVM (Model–View–ViewModel)

**Components:**

* `UserRepository`: provides CRUD methods (Retrofit interface)
* `UserViewModel`: a single shared ViewModel instance used across all screens via Hilt (`hiltViewModel()`) to keep state consistent
* `UserListScreen`, `UserEditScreen`, `UserCreateScreen`: Compose UIs
* Navigation: `NavDisplay` with `NavKey` targets defined via a sealed interface `NavHostDestination`
* `Hilt`: provides DI for `Retrofit`, `Repository`, and `ViewModel`

### 6.1 Navigation Model

We use androidx.navigation3 APIs with a sealed destination model and animated transitions.

```kotlin
sealed interface NavHostDestination

@Serializable object UserListKey : NavKey, NavHostDestination
@Serializable data class UserEditKey(val userId: String) : NavKey, NavHostDestination
@Serializable object UserCreateKey : NavKey, NavHostDestination
```

### 6.2 Nav Host & Transitions

`NavDisplay` renders a remembered back stack of `NavHostDestination` keys. Screen changes are animated:
- Edit screen uses a vertical slide transition (`slideVerticalAnimation`).
- Create screen uses a horizontal slide transition (`slideHorizontalAnimation`).

All screens obtain the same `UserViewModel` instance via `hiltViewModel()`.

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
