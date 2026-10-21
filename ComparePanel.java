package com.atmos;

import javax.swing.*;
import java.awt.*;

/**
 * Interactive comparison view allowing side-by-side analysis of two cities.
 */
public class ComparePanel extends JPanel {

    private final JTextField txtCity1;
    private final JTextField txtCity2;
    private final JButton btnCompare;

    private final JLabel lblCity1Name, lblCity1Temp, lblCity1Hum, lblCity1Score;
    private final JLabel lblCity2Name, lblCity2Temp, lblCity2Hum, lblCity2Score;
    private final JLabel lblVerdict;

    public ComparePanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Utils.COLOR_BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Search Bar
        JPanel topBar = Utils.createCardPanel();
        topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));

        txtCity1 = new JTextField("Mumbai", 12);
        txtCity2 = new JTextField("London", 12);
        btnCompare = new JButton("Compare Cities");
        btnCompare.setBackground(Utils.COLOR_ACCENT);
        btnCompare.setForeground(Color.WHITE);
        btnCompare.setFocusPainted(false);

        topBar.add(Utils.createStyledLabel("City 1:", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY));
        topBar.add(txtCity1);
        topBar.add(Utils.createStyledLabel("vs  City 2:", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY));
        topBar.add(txtCity2);
        topBar.add(btnCompare);

        // Center Comparison Cards
        JPanel centerGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        centerGrid.setOpaque(false);

        JPanel card1 = Utils.createCardPanel();
        card1.setLayout(new GridLayout(4, 1, 10, 10));
        lblCity1Name = Utils.createStyledLabel("City 1: --", Utils.FONT_HEADER, Utils.COLOR_ACCENT);
        lblCity1Temp = Utils.createStyledLabel("Temperature: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblCity1Hum = Utils.createStyledLabel("Humidity: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblCity1Score = Utils.createStyledLabel("Comfort Score: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        card1.add(lblCity1Name); card1.add(lblCity1Temp); card1.add(lblCity1Hum); card1.add(lblCity1Score);

        JPanel card2 = Utils.createCardPanel();
        card2.setLayout(new GridLayout(4, 1, 10, 10));
        lblCity2Name = Utils.createStyledLabel("City 2: --", Utils.FONT_HEADER, Utils.COLOR_ACCENT);
        lblCity2Temp = Utils.createStyledLabel("Temperature: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblCity2Hum = Utils.createStyledLabel("Humidity: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblCity2Score = Utils.createStyledLabel("Comfort Score: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        card2.add(lblCity2Name); card2.add(lblCity2Temp); card2.add(lblCity2Hum); card2.add(lblCity2Score);

        centerGrid.add(card1);
        centerGrid.add(card2);

        // Bottom Verdict Panel
        JPanel cardVerdict = Utils.createCardPanel();
        cardVerdict.setLayout(new BorderLayout());
        JLabel lblVerdictTitle = Utils.createStyledLabel("Analytical Comparison Verdict", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);
        lblVerdict = Utils.createStyledLabel("Enter two cities above and click Compare.", Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY);
        cardVerdict.add(lblVerdictTitle, BorderLayout.NORTH);
        cardVerdict.add(lblVerdict, BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(centerGrid, BorderLayout.CENTER);
        add(cardVerdict, BorderLayout.SOUTH);

        btnCompare.addActionListener(e -> executeComparison());
    }

    private void executeComparison() {
        String c1 = txtCity1.getText().trim();
        String c2 = txtCity2.getText().trim();

        if (c1.isEmpty() || c2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter two valid city names.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnCompare.setText("Loading...");
        btnCompare.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            WeatherData d1 = null, d2 = null;
            String errorMsg = null;

            @Override
            protected Void doInBackground() {
                try {
                    d1 = WeatherAPI.fetchWeatherData(c1);
                    d2 = WeatherAPI.fetchWeatherData(c2);
                } catch (Exception ex) {
                    errorMsg = ex.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                btnCompare.setText("Compare Cities");
                btnCompare.setEnabled(true);

                if (errorMsg != null || d1 == null || d2 == null) {
                    JOptionPane.showMessageDialog(ComparePanel.this, "Comparison failed: " + errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                WeatherAnalyzer.ComfortReport r1 = WeatherAnalyzer.calculateComfortScore(d1);
                WeatherAnalyzer.ComfortReport r2 = WeatherAnalyzer.calculateComfortScore(d2);

                lblCity1Name.setText("City: " + d1.getCityName());
                lblCity1Temp.setText(String.format("Temperature: %.1f °C (Feels: %.1f°C)", d1.getTemperature(), d1.getApparentTemperature()));
                lblCity1Hum.setText(String.format("Humidity: %d%% | Wind: %.1f km/h", d1.getHumidity(), d1.getWindSpeed()));
                lblCity1Score.setText(String.format("Comfort Score: %d/100 (%s)", r1.getScore(), r1.getRating()));

                lblCity2Name.setText("City: " + d2.getCityName());
                lblCity2Temp.setText(String.format("Temperature: %.1f °C (Feels: %.1f°C)", d2.getTemperature(), d2.getApparentTemperature()));
                lblCity2Hum.setText(String.format("Humidity: %d%% | Wind: %.1f km/h", d2.getHumidity(), d2.getWindSpeed()));
                lblCity2Score.setText(String.format("Comfort Score: %d/100 (%s)", r2.getScore(), r2.getRating()));

                lblVerdict.setText(WeatherAnalyzer.determineBetterCity(d1, d2));
            }
        };
        worker.execute();
    }
}