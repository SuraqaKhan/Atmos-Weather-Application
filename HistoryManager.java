package com.atmos;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the runtime collection of search history.
 * Demonstrates the usage of Java Collection Framework (ArrayList).
 */
public class HistoryManager {
    private final List<String> historyList;

    public HistoryManager() {
        this.historyList = new ArrayList<>();
        loadHistory();
    }

    public void addSearch(String city) {
        if (city == null || city.trim().isEmpty()) return;
        String formatted = city.trim();

        // Remove duplicate entry if present to push to top
        historyList.removeIf(s -> s.equalsIgnoreCase(formatted));
        historyList.add(0, formatted);

        // Retain max 20 history items
        if (historyList.size() > 20) {
            historyList.remove(historyList.size() - 1);
        }

        saveHistory();
    }

    public List<String> getHistory() {
        return new ArrayList<>(historyList);
    }

    public void clearHistory() {
        historyList.clear();
        saveHistory();
    }

    private void loadHistory() {
        List<String> loaded = FileManager.loadHistoryFromFile();
        historyList.clear();
        historyList.addAll(loaded);
    }

    private void saveHistory() {
        FileManager.saveHistoryToFile(historyList);
    }
}