package com.weatherapp.api;
import com.weatherapp.models.ForecastData;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherapp.models.WeatherData;

public class WeatherApiClient {

    private static final String BASE_URL =
            "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_URL =
            "https://api.openweathermap.org/data/2.5/forecast";

    private static final String API_KEY =
            System.getenv("OPENWEATHER_API_KEY");

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WeatherApiClient() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        objectMapper = new ObjectMapper();
    }

    public WeatherData getCurrentWeather(String city)
            throws IOException, InterruptedException {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException(
                    "OPENWEATHER_API_KEY environment variable is not set.");
        }

        String encodedCity =
                URLEncoder.encode(city, StandardCharsets.UTF_8);

        String url = BASE_URL
                + "?q=" + encodedCity
                + "&appid=" + API_KEY
                + "&units=metric";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request,
                        HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Weather API request failed. HTTP status: "
                            + response.statusCode()
                            + " - " + response.body());
        }

        return objectMapper.readValue(
                response.body(), WeatherData.class);
    }
    public ForecastData getForecast(String city)
            throws IOException, InterruptedException {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new IllegalStateException(
                    "OPENWEATHER_API_KEY environment variable is not set.");
        }

        String encodedCity =
                URLEncoder.encode(city, StandardCharsets.UTF_8);

        String url = FORECAST_URL
                + "?q=" + encodedCity
                + "&appid=" + API_KEY
                + "&units=metric";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request,
                        HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Forecast API request failed. HTTP status: "
                            + response.statusCode()
                            + " - " + response.body());
        }

        return objectMapper.readValue(
                response.body(), ForecastData.class);
    }
    
}