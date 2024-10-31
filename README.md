# Currency Converter App

A simple and efficient currency converter application that allows users to convert between different currencies using real-time exchange rates.

## Features

- Real-time currency conversion
- Support for multiple currencies
- User-friendly interface
- Accurate exchange rates from reliable API
- Offline mode with cached exchange rates

## Installation

Clone the repository
```bash
git clone https://github.com/tgound1403/curcon.git
```
Add ExchangeRateAPI Key to local.properties as
```
EXCHANGE_RATE_API_KEY="YOUR_API_KEY"
```
## Technical Stack
- **Platform:** Android
- **Language:** Kotlin

### Architecture & Libraries
- **Architecture Pattern:** MVVM (Model-View-ViewModel)
- **Dependency Injection:** Hilt
- **Local Database:** Room
- **Testing:** MockK
- **API Client:** [Retrofit](https://square.github.io/retrofit/)
- **Coroutines:** For asynchronous operations

### Layer Structure
- Presentation Layer (MVVM)
  - ViewModel depends on UseCases
  - Fragments/Activities observe ViewModel
  - Uses [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for dependency injection

- Domain Layer
  - Contains business logic and interfaces
  - Pure Kotlin module with no Android dependencies
  - UseCases orchestrate data flow between layers

- Data Layer
  - Implements repository interfaces from domain layer
  - Handles data sources ([Room](https://developer.android.com/training/data-storage/room) and API)
  - Manages data mapping between domain and data layers

### Data Flow
1. UI (Fragment/Activity) → ViewModel
2. ViewModel → UseCase
3. UseCase → Repository Interface
4. Repository Implementation → Local (Room) / Remote (API) data sources

This architecture ensures:
- Clear separation of concerns
- Easy testability (particularly with [MockK](https://mockk.io/))
- Independence of layers
- Single source of truth
- Maintainable and scalable codebase

## Demo Video link
https://drive.google.com/drive/folders/1m456ovyJxLoUFbeLTyD0S6aUJwL7UgB5?usp=sharing
        
