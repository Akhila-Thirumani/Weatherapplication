package com.weatherapp;

import javax.swing.SwingUtilities;

import com.weatherapp.gui.WeatherAppGUI;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            WeatherAppGUI weatherApp = new WeatherAppGUI();
            weatherApp.setVisible(true);
        });
    }
}