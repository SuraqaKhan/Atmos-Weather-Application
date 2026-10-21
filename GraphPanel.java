package com.atmos;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

/**
 * Embedded graph view rendering 7-day trend metrics using JFreeChart.
 * Inherits from JPanel.
 */
public class GraphPanel extends JPanel {

    private final DefaultCategoryDataset tempDataset;
    private final DefaultCategoryDataset humidityDataset;
    private final JLabel lblSummary;

    public GraphPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Utils.COLOR_BG_DARK);

        tempDataset = new DefaultCategoryDataset();
        humidityDataset = new DefaultCategoryDataset();

        JFreeChart tempChart = createChart("7-Day Temperature Trend (°C)", tempDataset, new Color(231, 76, 60));
        JFreeChart humidityChart = createChart("7-Day Humidity Trend (%)", humidityDataset, new Color(52, 152, 219));

        JPanel chartsContainer = new JPanel(new GridLayout(2, 1, 15, 15));
        chartsContainer.setOpaque(false);

        ChartPanel cpTemp = new ChartPanel(tempChart);
        ChartPanel cpHum = new ChartPanel(humidityChart);
        cpTemp.setOpaque(false);
        cpHum.setOpaque(false);

        chartsContainer.add(cpTemp);
        chartsContainer.add(cpHum);

        // Summary Card
        JPanel cardSummary = Utils.createCardPanel();
        cardSummary.setLayout(new BorderLayout());
        JLabel lblTitle = Utils.createStyledLabel("Historical Analysis Summary", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);
        lblSummary = Utils.createStyledLabel("Search a city to generate trend summary.", Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY);

        cardSummary.add(lblTitle, BorderLayout.NORTH);
        cardSummary.add(lblSummary, BorderLayout.CENTER);

        add(chartsContainer, BorderLayout.CENTER);
        add(cardSummary, BorderLayout.SOUTH);
    }

    public void updateGraphData(WeatherData data) {
        tempDataset.clear();
        humidityDataset.clear();

        if (data == null || data.getHistoricalTemps() == null) return;

        double[] temps = data.getHistoricalTemps();
        double[] humidity = data.getHistoricalHumidity();
        String[] dates = data.getHistoricalDates();

        double sumTemp = 0, maxTemp = Double.MIN_VALUE, minTemp = Double.MAX_VALUE;
        double sumHum = 0;

        for (int i = 0; i < temps.length; i++) {
            String label = (dates != null && i < dates.length && dates[i] != null) ? dates[i].substring(5) : "Day " + (i + 1);
            tempDataset.addValue(temps[i], "Temperature", label);
            humidityDataset.addValue(humidity[i], "Humidity", label);

            sumTemp += temps[i];
            sumHum += humidity[i];
            if (temps[i] > maxTemp) maxTemp = temps[i];
            if (temps[i] < minTemp) minTemp = temps[i];
        }

        double avgTemp = sumTemp / temps.length;
        double avgHum = sumHum / humidity.length;

        lblSummary.setText(String.format(
                "<html>Over the last 7 days in <b>%s</b>, maximum temperature reached <b>%.1f°C</b> with a minimum of <b>%.1f°C</b>.<br>" +
                        "Weekly Average Temperature: <b>%.1f°C</b> | Weekly Average Humidity: <b>%.1f%%</b>.</html>",
                data.getCityName(), maxTemp, minTemp, avgTemp, avgHum));
    }

    private JFreeChart createChart(String title, DefaultCategoryDataset dataset, Color seriesColor) {
        JFreeChart chart = ChartFactory.createLineChart(
                title, "Date", "Value", dataset, PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(Utils.COLOR_CARD_BG);
        chart.getTitle().setPaint(Utils.COLOR_TEXT_PRIMARY);
        chart.getTitle().setFont(Utils.FONT_HEADER);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Utils.COLOR_CARD_BG);
        plot.setDomainGridlinePaint(Utils.COLOR_BORDER);
        plot.setRangeGridlinePaint(Utils.COLOR_BORDER);
        plot.setOutlineVisible(false);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, seriesColor);
        renderer.setSeriesStroke(0, new BasicStroke(3.0f));
        plot.setRenderer(renderer);

        return chart;
    }
}