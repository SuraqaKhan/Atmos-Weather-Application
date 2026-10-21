package com.atmos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all local File I/O operations including history persistence
 * and exportable report generation.
 */
public class FileManager {
    private static final String HISTORY_FILE = "atmos_history.txt";

    public static void saveHistoryToFile(List<String> history) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            for (String item : history) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving search history: " + e.getMessage());
        }
    }

    public static List<String> loadHistoryFromFile() {
        List<String> history = new ArrayList<>();
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return history;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    history.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading search history: " + e.getMessage());
        }
        return history;
    }

    public static boolean exportWeatherReport(String filePath, WeatherData data, WeatherAnalyzer.ComfortReport report, List<String> insights) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("========================================================\n");
            writer.write("           ATMOS WEATHER INTELLIGENCE REPORT            \n");
            writer.write("========================================================\n");
            writer.write("Generated Date : " + Utils.getFormattedDate() + "\n");
            writer.write("Generated Time : " + Utils.getFormattedTime() + "\n");
            writer.write("Location       : " + data.getCityName() + "\n");
            writer.write("--------------------------------------------------------\n\n");

            writer.write("[CURRENT METRICS]\n");
            writer.write(String.format(" Temperature         : %.1f °C\n", data.getTemperature()));
            writer.write(String.format(" Feels Like          : %.1f °C\n", data.getApparentTemperature()));
            writer.write(String.format(" Weather Condition   : %s\n", data.getWeatherCondition()));
            writer.write(String.format(" Relative Humidity   : %d %%\n", data.getHumidity()));
            writer.write(String.format(" Surface Pressure    : %.1f hPa\n", data.getPressure()));
            writer.write(String.format(" Wind Speed          : %.1f km/h\n", data.getWindSpeed()));
            writer.write(String.format(" UV Index            : %d\n", data.getUvIndex()));
            writer.write(String.format(" Visibility          : %.1f km\n", data.getVisibility()));
            writer.write(String.format(" Sunrise / Sunset    : %s / %s\n\n", data.getSunrise(), data.getSunset()));

            writer.write("[COMFORT SCORE INDEX]\n");
            writer.write(String.format(" Score  : %d / 100\n", report.getScore()));
            writer.write(String.format(" Rating : %s\n", report.getRating()));
            writer.write(" Summary: " + report.getExplanation() + "\n\n");

            writer.write("[SMART INTELLIGENCE & RECOMMENDATIONS]\n");
            for (int i = 0; i < insights.size(); i++) {
                writer.write(String.format(" %d. %s\n", (i + 1), insights.get(i)));
            }

            writer.write("\n========================================================\n");
            writer.write("           END OF REPORT - ATMOS ANALYTICS             \n");
            writer.write("========================================================\n");

            return true;
        } catch (IOException e) {
            System.err.println("Failed to write report file: " + e.getMessage());
            return false;
        }
    }
}