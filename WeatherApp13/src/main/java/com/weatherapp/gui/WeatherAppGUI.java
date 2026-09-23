package com.weatherapp.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.weatherapp.alerts.WeatherAlerts;
import com.weatherapp.api.WeatherApiClient;
import com.weatherapp.cache.CacheManager;
import com.weatherapp.models.WeatherData;

public class WeatherAppGUI extends JFrame {

    private final JTextField cityField;
    private final JLabel cityLabel;
    private final JLabel temperatureLabel;
    private final JLabel feelsLikeLabel;
    private final JLabel humidityLabel;
    private final JLabel conditionLabel;
    private final JLabel windLabel;
    private final JLabel statusLabel;
    private final JTextArea forecastArea;

    private final WeatherApiClient apiClient;
    private final CacheManager cacheManager;
    private final WeatherAlerts weatherAlerts;

    public WeatherAppGUI() {

        apiClient = new WeatherApiClient();
        cacheManager = new CacheManager();
        weatherAlerts = new WeatherAlerts();

        setTitle("Weather Application");
        setSize(650, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel();

        cityField = new JTextField(20);
        cityField.setText("Hyderabad");

        JButton searchButton = new JButton("Search Weather");

        searchPanel.add(new JLabel("City:"));
        searchPanel.add(cityField);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel weatherPanel = new JPanel(new GridLayout(6, 1, 5, 5));

        cityLabel = new JLabel("City: -");
        temperatureLabel = new JLabel("Temperature: -");
        feelsLikeLabel = new JLabel("Feels Like: -");
        humidityLabel = new JLabel("Humidity: -");
        conditionLabel = new JLabel("Condition: -");
        windLabel = new JLabel("Wind Speed: -");

        Font weatherFont = new Font("Arial", Font.PLAIN, 16);

        cityLabel.setFont(weatherFont);
        temperatureLabel.setFont(weatherFont);
        feelsLikeLabel.setFont(weatherFont);
        humidityLabel.setFont(weatherFont);
        conditionLabel.setFont(weatherFont);
        windLabel.setFont(weatherFont);

        weatherPanel.add(cityLabel);
        weatherPanel.add(temperatureLabel);
        weatherPanel.add(feelsLikeLabel);
        weatherPanel.add(humidityLabel);
        weatherPanel.add(conditionLabel);
        weatherPanel.add(windLabel);

        mainPanel.add(weatherPanel, BorderLayout.CENTER);

        forecastArea = new JTextArea(10, 40);
        forecastArea.setEditable(false);
        forecastArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JPanel forecastPanel = new JPanel(new BorderLayout());
        forecastPanel.setBorder(
                BorderFactory.createTitledBorder("Weather Forecast"));

        forecastPanel.add(
                new JScrollPane(forecastArea),
                BorderLayout.CENTER);

        mainPanel.add(forecastPanel, BorderLayout.SOUTH);

        statusLabel = new JLabel("Enter a city and click Search Weather.");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        add(mainPanel);

        searchButton.addActionListener(e -> searchWeather());

        cityField.addActionListener(e -> searchWeather());
    }

    private void searchWeather() {

        String city = cityField.getText().trim();

        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a city name.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        statusLabel.setText("Loading weather data...");
        
        SwingWorker<WeatherData, Void> worker =
                new SwingWorker<WeatherData, Void>() {

            @Override
            protected WeatherData doInBackground() throws Exception {

                WeatherData cachedWeather =
                        cacheManager.get(city);

                if (cachedWeather != null) {
                    statusLabel.setText("Loaded from cache.");
                    return cachedWeather;
                }

                WeatherData weather =
                        apiClient.getCurrentWeather(city);

                cacheManager.put(city, weather);

                return weather;
            }

            @Override
            protected void done() {

                try {

                    WeatherData weather = get();

                    displayWeather(weather);

                    loadForecast(city);

                    String alerts =
                            weatherAlerts.checkAlerts(weather);

                    if (!alerts.equals("No weather alerts.")) {

                        JOptionPane.showMessageDialog(
                                WeatherAppGUI.this,
                                alerts,
                                "Weather Alert",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    statusLabel.setText(
                            "Weather data loaded successfully.");

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    statusLabel.setText(
                            "Request was interrupted.");

                } catch (ExecutionException e) {

                    Throwable cause = e.getCause();

                    statusLabel.setText(
                            "Unable to retrieve weather data.");

                    JOptionPane.showMessageDialog(
                            WeatherAppGUI.this,
                            getErrorMessage(cause),
                            "Weather Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void displayWeather(WeatherData weather) {

        cityLabel.setText(
                "City: " + weather.getName()
                        + ", " + weather.getSys().getCountry());

        temperatureLabel.setText(
                String.format(
                        "Temperature: %.2f °C",
                        weather.getMain().getTemp()));

        feelsLikeLabel.setText(
                String.format(
                        "Feels Like: %.2f °C",
                        weather.getMain().getFeelsLike()));

        humidityLabel.setText(
                "Humidity: "
                        + weather.getMain().getHumidity()
                        + "%");

        conditionLabel.setText(
                "Condition: "
                        + weather.getWeather()[0].getMain()
                        + " - "
                        + weather.getWeather()[0].getDescription());

        windLabel.setText(
                String.format(
                        "Wind Speed: %.2f m/s",
                        weather.getWind().getSpeed()));
    }

    private void loadForecast(String city) {

        forecastArea.setText("Loading forecast...\n");

        SwingWorker<String, Void> worker =
                new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground() throws Exception {

                var forecast =
                        apiClient.getForecast(city);

                StringBuilder result =
                        new StringBuilder();

                result.append(
                        "Forecast for ")
                        .append(forecast.getCity().getName())
                        .append("\n\n");

                int count = Math.min(
                        8,
                        forecast.getList().length);

                for (int i = 0; i < count; i++) {

                    var item = forecast.getList()[i];

                    result.append(
                            String.format(
                                    "Temperature: %.2f °C | "
                                    + "Humidity: %d%% | "
                                    + "%s%n",
                                    item.getMain().getTemp(),
                                    item.getMain().getHumidity(),
                                    item.getWeather()[0]
                                            .getDescription()));
                }

                return result.toString();
            }

            @Override
            protected void done() {

                try {

                    forecastArea.setText(get());

                } catch (Exception e) {

                    forecastArea.setText(
                            "Unable to load forecast.");
                }
            }
        };

        worker.execute();
    }

    private String getErrorMessage(Throwable error) {

        if (error == null) {
            return "Unknown error occurred.";
        }

        String message = error.getMessage();

        if (message == null || message.isBlank()) {
            return "Unable to retrieve weather data.";
        }

        return message;
    }
}