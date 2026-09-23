
# Weather Application with API Integration

## Project Overview

The Weather Application is a Java-based desktop application that retrieves
real-time weather information using the OpenWeather API.

The application provides a simple Swing graphical user interface where users
can search for a city and view current weather conditions and forecast data.

The project demonstrates:

- HTTP client communication
- REST API integration
- JSON processing using Jackson
- Client-server communication
- Swing GUI development
- Weather data caching
- Network error handling
- Weather alerts and notifications
- Background API processing

---

## Features

### 1. Current Weather

Users can search for a city and view:

- City and country
- Current temperature
- Feels-like temperature
- Humidity
- Weather condition
- Weather description
- Wind speed

### 2. Weather Forecast

The application retrieves forecast information from the OpenWeather API
and displays upcoming forecast data in the GUI.

### 3. City Search

Users can enter a city name and retrieve weather information for that
location.

### 4. Caching

Weather results are stored temporarily using a thread-safe
`ConcurrentHashMap`.

Cached weather information is reused for up to 10 minutes to reduce
unnecessary API requests.

### 5. Weather Alerts

The application checks weather conditions and displays alerts for:

- Extreme temperatures
- Very low temperatures
- Strong winds
- Thunderstorms
- Tornado conditions

### 6. Error Handling

The application handles:

- Invalid API responses
- Network failures
- API request failures
- Request timeouts
- Interrupted requests
- Empty city input

### 7. Background Processing

API requests are performed using `SwingWorker` so that network operations
do not block the Swing user interface.

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Java Swing | Desktop graphical interface |
| Java HttpClient | HTTP communication |
| Jackson | JSON parsing |
| OpenWeather API | Weather data |
| Maven | Dependency management |
| JUnit 5 | Testing |
| ConcurrentHashMap | Weather caching |
| Eclipse IDE | Development environment |

---

## Project Structure

```text
WeatherApp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── weatherapp/
│   │   │           ├── Main.java
│   │   │           ├── ApiTest.java
│   │   │           │
│   │   │           ├── api/
│   │   │           │   └── WeatherApiClient.java
│   │   │           │
│   │   │           ├── models/
│   │   │           │   ├── WeatherData.java
│   │   │           │   └── ForecastData.java
│   │   │           │
│   │   │           ├── gui/
│   │   │           │   └── WeatherAppGUI.java
│   │   │           │
│   │   │           ├── cache/
│   │   │           │   └── CacheManager.java
│   │   │           │
│   │   │           ├── alerts/
│   │   │           │   └── WeatherAlerts.java
│   │   │           │
│   │   │           └── utils/
│   │   │
│   │   └── resources/
│   │
│   └── test/
│       ├── java/
│       └── resources/
│
├── pom.xml
└── README.md
