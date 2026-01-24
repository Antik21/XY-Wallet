# Apps AGENTS Guide

This folder contains client applications and the shared KMP module. The structure and responsibilities follow the official KMP layout: platform apps + a shared module.

## Contents of the apps folder

- `apps/composeApp` — Android app (Kotlin, Jetpack Compose, AndroidX Navigation, integration with the shared KMP module).
- `apps/iosApp` — iOS app (Swift, SwiftUI, KMP integration via SKIE).
- `apps/shared` — shared Kotlin Multiplatform module: shared logic in `commonMain`, platform implementations in `androidMain`/`iosMain`, iOS framework build `Shared`.

Architecture and structure sources:
- Kotlin Multiplatform: Project structure, source sets (`commonMain`, `androidMain`, `iosMain`).
- Android Developers: KMP project structure (androidApp/iosApp/shared).
- SKIE: official source for Swift ↔ KMP interop.

## Naming rules

- **Same names for shared entities.** A screen that exists on both platforms uses the same name: `HomeScreen.kt` and `HomeScreen.swift`. This also applies to ViewModel and state types (`HomeViewModel`, `HomeUiState`).
- **Identical feature names.** The feature folder name must match on Android and iOS: `feature/home`, `feature/cart`, etc.
- **File name = type name.** Class/struct/enum lives in a file with the same name.
- **Platform implementations.** For `expect/actual`, use the same type name (for example, `PushTokenProvider` in `commonMain`, `actual` implementations in `androidMain`/`iosMain`).

## Where to create new files

- **Shared business logic, models, use cases, state:**  
  `apps/shared/src/commonMain/kotlin/<package>/...`
- **Platform implementations (push, navigation, OS access):**  
  `apps/shared/src/androidMain/kotlin/...` and `apps/shared/src/iosMain/kotlin/...`
- **Android UI and navigation:**  
  `apps/composeApp/src/androidMain/kotlin/<package>/feature/<Feature>/...`
- **iOS UI and navigation:**  
  `apps/iosApp/iosApp/feature/<Feature>/...`

## File and folder strategy (Android/iOS mirroring)

- **Shared as the source of truth.** Shared logic and state live in `apps/shared`. Platforms consume it without duplication.
- **Mirrored feature structure.** Each feature keeps the same path and set of key entities in `composeApp` and `iosApp`. If you add `feature/profile/ProfileScreen` on Android, create `feature/profile/ProfileScreen` on iOS.
- **Platform navigation and push live in apps.** Each app uses its native navigation and push integration, while shared models/intents/events for screens stay in `shared`.
- **Unified names for screens and flows.** All cross-platform screens, states, and intents share identical names and base location in the tree.
- **Platform-specific via expect/actual.** Declare the interface/expect in `commonMain`, implement strictly in `androidMain`/`iosMain` with the same type name.
- **Minimal platform knowledge in shared.** `shared` does not depend on concrete navigation/push libraries; it provides interfaces and models for app-level integration.
