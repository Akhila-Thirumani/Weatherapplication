package com.weatherapp.alerts;

import com.weatherapp.models.WeatherData;

public class WeatherAlerts {

    public String checkAlerts(WeatherData weather) {

        StringBuilder alerts = new StringBuilder();

        double temperature = weather.getMain().getTemp();
        double windSpeed = weather.getWind().getSpeed();
        String condition = weather.getWeather()[0].getMain();

        if (temperature >= 40) {
            alerts.append("Extreme heat alert! Temperature is very high.\n");
        } else if (temperature <= 10) {
            alerts.append("Cold weather alert! Temperature is very low.\n");
        }

        if (windSpeed >= 15) {
            alerts.append("Strong wind alert! Wind speed is high.\n");
        }

        if (condition.equalsIgnoreCase("Thunderstorm")) {
            alerts.append("Thunderstorm alert! Please take precautions.\n");
        }

        if (condition.equalsIgnoreCase("Tornado")) {
            alerts.append("Severe weather alert! Tornado conditions detected.\n");
        }

        if (alerts.length() == 0) {
            return "No weather alerts.";
        }

        return alerts.toString().trim();
    }
}