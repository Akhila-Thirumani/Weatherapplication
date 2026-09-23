package com.weatherapp.cache;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.weatherapp.models.WeatherData;

public class CacheManager {

    private static final long CACHE_DURATION_SECONDS = 600;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public void put(String city, WeatherData weatherData) {
        cache.put(city.toLowerCase(), new CacheEntry(weatherData));
    }

    public WeatherData get(String city) {
        CacheEntry entry = cache.get(city.toLowerCase());

        if (entry == null) {
            return null;
        }

        if (Instant.now().getEpochSecond() - entry.timestamp
                > CACHE_DURATION_SECONDS) {
            cache.remove(city.toLowerCase());
            return null;
        }

        return entry.weatherData;
    }

    public boolean contains(String city) {
        return get(city) != null;
    }

    public void clear() {
        cache.clear();
    }

    private static class CacheEntry {

        private final WeatherData weatherData;
        private final long timestamp;

        CacheEntry(WeatherData weatherData) {
            this.weatherData = weatherData;
            this.timestamp = Instant.now().getEpochSecond();
        }
    }
}