# Project AGENTS Guide

This file describes the structure and maintenance conventions for the entire project.
It will be expanded over time.

## Agent Behavior (Global)

- Always respond to the user in Russian.
- Always write code comments in English.
- Avoid obvious comments. Comments must explain non-obvious or complex logic, or clarify why a specific line exists when it is not self-evident.

## Kotlin Multiplatform (KMP) Usage and References

Detailed and up-to-date information about using **Kotlin Multiplatform (KMP)** in a mobile application **must be taken from the official documentation and sample projects** listed below.

### Official Documentation
For architecture, setup, project structure, and recommended practices, refer to:

- Kotlin Multiplatform – Get Started  
  https://kotlinlang.org/docs/multiplatform/get-started.html

- Android Developers – Kotlin Multiplatform  
  https://developer.android.com/kotlin/multiplatform/

### Sample Projects
For answers to **basic and practical questions**, real project structure examples, and common patterns, refer to the official Android samples repository:

- Kotlin Multiplatform Samples  
  https://github.com/android/kotlin-multiplatform-samples/tree/main

### Swift ↔ KMP Bridge (iOS Integration)

For building a **bridge between Swift code and Kotlin Multiplatform (KMP)**, the project uses the **SKIE** library.

SKIE provides:
- Safer and more idiomatic Swift APIs for KMP code
- Improved interoperability with Swift Concurrency (`async/await`)
- Better mapping of Kotlin types to Swift
- Reduced boilerplate when consuming shared logic from iOS

#### SKIE Documentation
All usage details, supported features, configuration options, and best practices **must be taken from the official SKIE documentation**:

- SKIE Features & Documentation  
  https://skie.touchlab.co/features/

#### SKIE Source Code
The full source code of the library is publicly available and can be used for deeper understanding or debugging:

- SKIE GitHub Repository  
  https://github.com/touchlab/SKIE

### Context7 Requirement
When answering questions, explaining implementation details, or making architectural decisions related to **Kotlin Multiplatform** or **Swift ↔ KMP interoperability**, the AI agent **must use context7** to retrieve information from the sources listed above.

Rules:
- Always search context7 first for relevant sections in the provided documentation.
- Base explanations strictly on the retrieved documentation or sample code.
- Reference the specific documentation pages, features, or source modules used.
- If the required information is not found in context7, explicitly state that it is not covered in the referenced documentation.

These sources are considered the **single source of truth** for KMP usage and Swift interoperability in this project.

## Project modules

### Server module

All server-specific architecture and maintenance instructions are documented in:
`server/AGENTS.md`

Use that file as the single source of truth for server structure, libraries,
configuration, request/response formats, and extension guidelines.

## UI Kit (Platform-Specific Components)

UI Kit is **NOT** a shared KMP module. Each platform has its own UI Kit implementation with platform-specific code.

### Purpose
- Provide reusable UI components for each platform
- Maintain consistent naming and component structure across platforms
- Allow platform-specific customizations while keeping similar APIs

### Naming Convention
- All components **must** use the `UiKit` prefix
- Component names **must** be identical on both platforms (e.g., `UiKitButton`, `UiKitEmptyState`)
- File names **must** match component names

### Structure
- **Android**: Module `:uikit` at `apps/uikit` with sources in `apps/uikit/src/commonMain/kotlin/com/antik/wallet/uikit`
  - Uses Compose Multiplatform
  - Configured as Android-only library (not a KMP module)
- **iOS**: Directory `apps/iosApp/iosApp/ui-kit`
  - Uses SwiftUI
  - Part of the iOS app target

### Divergence Policy
- Components should have similar APIs and behavior across platforms
- Platform-specific components are allowed when they address platform-specific UI patterns
- Document any significant API differences between platforms in code comments
