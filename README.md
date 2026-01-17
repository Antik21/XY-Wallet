# XY-Wallet

## Application Overview
XY-Wallet is a simple Android and iOS app with a native, pleasant UI for secure management of Web3 wallet funds.

## Project Goals
- Build a hybrid native + KMP application.
- Study an approach where business logic is shared via KMP, while the UI is rendered natively on each platform.
- Apply simple but correct development practices.
- The project is primarily training and demonstration oriented.

## Project Infrastructure
### Server
- A lightweight Kotlin server built with Ktor.
- A simple, reliable architecture for maintenance, extension, and deployment.
- Shared modules with the apps for communication, such as routes/endpoints and models.

### Mobile Apps
- MVI (MVVM+ with UDF) approach in the app.
- The KMP core covers screen ViewModel logic, plus domain and data layers.
- Android uses Jetpack Compose for UI rendering.
- iOS uses SwiftUI.
- Bridge libraries for convenient communication from Swift code to Kotlin ViewModels.

## Application Features
TODO - will be added as the implementation progresses.
