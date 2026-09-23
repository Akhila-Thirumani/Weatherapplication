package com.weatherapp;

import com.weatherapp.api.WeatherApiClient;
import com.weatherapp.models.WeatherData;

public class ApiTest {

    public static void main(String[] args) {

        try {
            WeatherApiClient client = new WeatherApiClient();

            WeatherData weather = client.getCurrentWeather("Hyderabad");

            System.out.println("===== Weather API Test =====");
            System.out.println("City: " + weather.getName());
            System.out.println("Country: " + weather.getSys().getCountry());
            System.out.println("Temperature: " + weather.getMain().getTemp() + " °C");
            System.out.println("Feels Like: " + weather.getMain().getFeelsLike() + " °C");
            System.out.println("Humidity: " + weather.getMain().getHumidity() + "%");
            System.out.println("Condition: " + weather.getWeather()[0].getMain());
            System.out.println("Description: " + weather.getWeather()[0].getDescription());
            System.out.println("Wind Speed: " + weather.getWind().getSpeed() + " m/s");

        } catch (Exception e) {
            System.out.println("Failed to fetch weather data.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}