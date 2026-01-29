# Apps AGENTS Guide

This folder contains client applications and the shared KMP module. The structure and responsibilities follow the official KMP layout: platform apps + a shared module.

## Contents of the apps folder

- `apps/composeApp` — Android app (Kotlin, Jetpack Compose, AndroidX Navigation, integration with the shared KMP module).
- `apps/iosApp` — iOS app (Swift, SwiftUI, KMP integration via SKIE).
- `apps/shared` — shared Kotlin Multiplatform module: shared logic in `commonMain`, platform implementations in `androidMain`/`iosMain`, iOS framework build `Shared`.
- `apps/uikit` — Android UI Kit module with reusable Compose components (Android-only, not a KMP module).

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
  `apps/shared/src/commonMain/kotlin/com/antik/wallet/feature/<FeatureGroup>/<Feature>/...`
  - Base classes and utilities: `apps/shared/src/commonMain/kotlin/com/antik/wallet/feature/base/...`
  - Example: `apps/shared/src/commonMain/kotlin/com/antik/wallet/feature/museum/detail/DetailViewModel.kt`
- **Platform implementations (push, navigation, OS access):**
  `apps/shared/src/androidMain/kotlin/...` and `apps/shared/src/iosMain/kotlin/...`
- **Android UI and navigation:**
  `apps/composeApp/src/androidMain/kotlin/com/antik/wallet/feature/<FeatureGroup>/<Feature>/...`
  - Example: `apps/composeApp/src/androidMain/kotlin/com/antik/wallet/feature/museum/detail/DetailScreen.kt`
- **iOS UI and navigation:**
  `apps/iosApp/iosApp/feature/<FeatureGroup>/<Feature>/...`
  - Example: `apps/iosApp/iosApp/feature/museum/detail/DetailScreen.swift`
- **Android UI Kit components:**
  `apps/uikit/src/commonMain/kotlin/com/antik/wallet/uikit/...`
- **iOS UI Kit components:**
  `apps/iosApp/iosApp/ui-kit/...`

## File and folder strategy (Android/iOS mirroring)

- **Shared as the source of truth.** Shared logic and state live in `apps/shared`. Platforms consume it without duplication.
- **Mirrored feature structure.** Each feature keeps the same path and set of key entities in `composeApp` and `iosApp`. If you add `feature/profile/ProfileScreen` on Android, create `feature/profile/ProfileScreen` on iOS.
- **Platform navigation and push live in apps.** Each app uses its native navigation and push integration, while shared models/intents/events for screens stay in `shared`.
- **Unified names for screens and flows.** All cross-platform screens, states, and intents share identical names and base location in the tree.
- **Platform-specific via expect/actual.** Declare the interface/expect in `commonMain`, implement strictly in `androidMain`/`iosMain` with the same type name.
- **Minimal platform knowledge in shared.** `shared` does not depend on concrete navigation/push libraries; it provides interfaces and models for app-level integration.

## UI Kit (Platform-Specific Components)

UI Kit is **NOT** a shared KMP module. Each platform has its own UI Kit implementation.

- **Purpose:** Reusable UI components with consistent naming across platforms.
- **Naming:** All components must use the `UiKit` prefix and have identical names on both platforms (e.g., `UiKitButton`, `UiKitEmptyState`).
- **Android:** Module `:uikit` at `apps/uikit` with sources in `apps/uikit/src/commonMain/kotlin/com/antik/wallet/uikit` (Android-only, uses Compose).
- **iOS:** Directory `apps/iosApp/iosApp/ui-kit` (SwiftUI components).
- **Divergence:** Platform-specific components are allowed for platform-specific UI patterns. Deviations from cross-platform naming should be documented.
