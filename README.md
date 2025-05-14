# ForexWatch

ForexWatch is an Android application that displays exchange rates relative to the Euro (EUR).  
The data is fetched from the [Fixer.io](https://fixer.io) API.

## Configuration

To run the app with live data, create the following files:

secrets.dev.properties
secrets.prod.properties

Each file must contain:

API_KEY=your_api_key_value

Alternatively, use the `mock` build variant to run the app with mock data from local JSON files.

## Architecture

- Modular Clean Architecture (MVI)
- Kotlin Coroutines, Flow, StateFlow
- Retrofit, OkHttp, Room
- Dependency Injection with Hilt
- 100% unit test coverage for use cases, repositories, services, mappers

## Build

```bash
./gradlew assembleDebug

![image](https://github.com/user-attachments/assets/50c95491-8f61-4e04-92e2-9ec7948eb823)
![image](https://github.com/user-attachments/assets/6e02f8f4-5f04-4f44-85fc-f2c21ff16331)

