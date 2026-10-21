package com.atmos;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelligent analytics engine that processes raw weather parameters to compute comfort scores,
 * generate insights, and evaluate environmental metrics.
 */
public class WeatherAnalyzer {

    public static class ComfortReport {
        private final int score;
        private final String rating;
        private final String explanation;

        public ComfortReport(int score, String rating, String explanation) {
            this.score = score;
            this.rating = rating;
            this.explanation = explanation;
        }

        public int getScore() { return score; }
        public String getRating() { return rating; }
        public String getExplanation() { return explanation; }
    }

    // Method Overloading Example 1: Compute comfort score from WeatherData object
    public static ComfortReport calculateComfortScore(WeatherData data) {
        if (data == null) return new ComfortReport(0, "N/A", "No data available.");
        return calculateComfortScore(
                data.getTemperature(),
                data.getHumidity(),
                data.getWindSpeed(),
                data.getVisibility(),
                data.getUvIndex()
        );
    }

    // Method Overloading Example 2: Compute comfort score from primitive metrics directly
    public static ComfortReport calculateComfortScore(double temp, int humidity, double windSpeed, double visibility, int uv) {
        double score = 100.0;

        // Temperature penalty (Ideal range: 20°C - 25°C)
        if (temp < 15) score -= (15 - temp) * 2.0;
        else if (temp > 26) score -= (temp - 26) * 2.5;

        // Humidity penalty (Ideal range: 30% - 60%)
        if (humidity < 30) score -= (30 - humidity) * 0.5;
        else if (humidity > 60) score -= (humidity - 60) * 0.8;

        // Wind Speed penalty (> 20 km/h)
        if (windSpeed > 20) score -= (windSpeed - 20) * 0.7;

        // UV Index penalty (> 5)
        if (uv > 5) score -= (uv - 5) * 3.0;

        // Visibility penalty (< 10 km)
        if (visibility < 10) score -= (10 - visibility) * 2.5;

        // Clamp score between 0 and 100
        int finalScore = Math.max(0, Math.min(100, (int) Math.round(score)));

        String rating;
        String explanation;

        if (finalScore >= 85) {
            rating = "Excellent";
            explanation = "Optimal weather conditions with balanced thermal comfort and high air clarity.";
        } else if (finalScore >= 70) {
            rating = "Good";
            explanation = "Pleasant weather overall, suitable for outdoor plans with minor variations.";
        } else if (finalScore >= 50) {
            rating = "Moderate";
            explanation = "Fair conditions; noticeable humidity, temperature offset, or wind speed present.";
        } else {
            rating = "Poor";
            explanation = "Suboptimal environmental conditions. Caution recommended for outdoor activities.";
        }

        return new ComfortReport(finalScore, rating, explanation);
    }

    // Automated Insight Generation
    public static List<String> generateInsights(WeatherData data) {
        List<String> insights = new ArrayList<>();
        if (data == null) return insights;

        if (data.getTemperature() >= 22 && data.getTemperature() <= 28 && data.getHumidity() <= 60 && data.getWindSpeed() < 15) {
            insights.add("Excellent weather for outdoor activities and commuting.");
        }
        if (data.getWeatherCode() >= 51 || data.getWeatherCondition().toLowerCase().contains("rain")) {
            insights.add("Precipitation detected. Carrying an umbrella is recommended.");
        }
        if (data.getUvIndex() >= 6) {
            insights.add("High UV exposure detected. Wear sunscreen and protective sunglasses.");
        }
        if (data.getVisibility() < 5.0) {
            insights.add("Visibility is below normal. Drive carefully in reduced clarity.");
        }
        if (data.getWindSpeed() > 25.0) {
            insights.add("Strong wind gusts expected. Secure loose outdoor objects.");
        }
        if (Math.abs(data.getApparentTemperature() - data.getTemperature()) >= 3.0) {
            if (data.getApparentTemperature() > data.getTemperature()) {
                insights.add("Air feels hotter than the recorded temperature due to ambient humidity.");
            } else {
                insights.add("Wind chill effect makes the air feel significantly cooler than measured.");
            }
        }
        if (data.getHumidity() > 80) {
            insights.add("Unusually high humidity levels detected; heavy thermal burden present.");
        }
        if (insights.isEmpty()) {
            insights.add("Stable ambient atmospheric metrics recorded across all parameters.");
        }

        return insights;
    }

    // Compare two cities and determine which has better weather conditions
    public static String determineBetterCity(WeatherData c1, WeatherData c2) {
        if (c1 == null || c2 == null) return "Insufficient data for comparison.";
        ComfortReport r1 = calculateComfortScore(c1);
        ComfortReport r2 = calculateComfortScore(c2);

        if (r1.getScore() > r2.getScore()) {
            return c1.getCityName() + " offers better overall weather conditions (" + r1.getScore() + "/100 vs " + r2.getScore() + "/100).";
        } else if (r2.getScore() > r1.getScore()) {
            return c2.getCityName() + " offers better overall weather conditions (" + r2.getScore() + "/100 vs " + r1.getScore() + "/100).";
        } else {
            return "Both cities offer comparable weather comfort ratings (" + r1.getScore() + "/100).";
        }
    }
}