package com.atmos;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) encapsulating weather metrics for a specific location.
 * Demonstrates Encapsulation through private fields and public getters/setters.
 */
public class WeatherData {
    private String cityName;
    private double temperature;
    private double apparentTemperature;
    private int humidity;
    private double pressure;
    private double windSpeed;
    private double visibility;
    private int uvIndex;
    private int weatherCode;
    private String weatherCondition;
    private String sunrise;
    private String sunset;
    private LocalDateTime timestamp;

    // Daily historical/forecast data (7 days)
    private double[] historicalTemps;
    private double[] historicalHumidity;
    private String[] historicalDates;

    public WeatherData() {
        this.timestamp = LocalDateTime.now();
        this.historicalTemps = new double[7];
        this.historicalHumidity = new double[7];
        this.historicalDates = new String[7];
    }

    public WeatherData(String cityName, double temperature, int humidity, double windSpeed, String weatherCondition) {
        this();
        this.cityName = cityName;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.weatherCondition = weatherCondition;
    }

    // Encapsulated Getters and Setters
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getApparentTemperature() { return apparentTemperature; }
    public void setApparentTemperature(double apparentTemperature) { this.apparentTemperature = apparentTemperature; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public double getVisibility() { return visibility; }
    public void setVisibility(double visibility) { this.visibility = visibility; }

    public int getUvIndex() { return uvIndex; }
    public void setUvIndex(int uvIndex) { this.uvIndex = uvIndex; }

    public int getWeatherCode() { return weatherCode; }
    public void setWeatherCode(int weatherCode) { this.weatherCode = weatherCode; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }

    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public double[] getHistoricalTemps() { return historicalTemps; }
    public void setHistoricalTemps(double[] historicalTemps) { this.historicalTemps = historicalTemps; }

    public double[] getHistoricalHumidity() { return historicalHumidity; }
    public void setHistoricalHumidity(double[] historicalHumidity) { this.historicalHumidity = historicalHumidity; }

    public String[] getHistoricalDates() { return historicalDates; }
    public void setHistoricalDates(String[] historicalDates) { this.historicalDates = historicalDates; }

    @Override
    public String toString() {
        return String.format("%s: %.1f°C, %s, Humidity: %d%%, Wind: %.1f km/h",
                cityName, temperature, weatherCondition, humidity, windSpeed);
    }
}