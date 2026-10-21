package com.atmos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Interface defining view navigation contract.
 */
interface Navigable {
    void navigateTo(String cardName);
}

/**
 * Primary Desktop GUI Application Frame for ATMOS.
 */
public class Dashboard extends JFrame implements Navigable {

    private final HistoryManager historyManager;
    private WeatherData currentData;

    // Search and Top Nav components
    private JTextField txtSearch;
    private JLabel lblCurrentCityHeader;
    private JLabel lblDateTimeHeader;

    // View Panels
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private GraphPanel graphPanel;
    private ComparePanel comparePanel;
    private JPanel historyViewPanel;

    // Dashboard Metric Labels
    private JLabel lblTemp, lblFeelsLike, lblCondition, lblHumidity, lblPressure;
    private JLabel lblWind, lblVisibility, lblUv, lblSunrise, lblSunset;
    private JLabel lblComfortScore, lblComfortRating, lblComfortDesc;
    private JPanel panelInsightsContainer;

    public Dashboard() {
        this.historyManager = new HistoryManager();

        setTitle("ATMOS - Weather Intelligence & Analytics");
        setSize(1200, 780);
        setMinimumSize(new Dimension(1000, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();

        // Initial Default Fetch
        fetchCityWeather("Mumbai");
    }

    private void initUI() {
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Utils.COLOR_BG_DARK);

        rootPanel.add(createSidebarPanel(), BorderLayout.WEST);

        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setOpaque(false);
        rightContainer.add(createTopNavPanel(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setOpaque(false);

        // Register Views
        mainContentPanel.add(createDashboardViewPanel(), "DASHBOARD");

        graphPanel = new GraphPanel();
        mainContentPanel.add(graphPanel, "HISTORICAL");

        comparePanel = new ComparePanel();
        mainContentPanel.add(comparePanel, "COMPARE");

        historyViewPanel = createHistoryViewPanel();
        mainContentPanel.add(historyViewPanel, "HISTORY");

        mainContentPanel.add(createAboutViewPanel(), "ABOUT");

        rightContainer.add(mainContentPanel, BorderLayout.CENTER);
        rootPanel.add(rightContainer, BorderLayout.CENTER);

        setContentPane(rootPanel);
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(Utils.COLOR_SIDEBAR);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Utils.COLOR_BORDER));

        // Brand Logo
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        brandPanel.setOpaque(false);
        JLabel lblLogo = Utils.createStyledLabel("ATMOS", Utils.FONT_TITLE, Utils.COLOR_TEXT_PRIMARY);
        JLabel lblSub = Utils.createStyledLabel("Analytics", Utils.FONT_SMALL, Utils.COLOR_ACCENT);
        brandPanel.add(lblLogo);
        brandPanel.add(lblSub);

        // Navigation Buttons
        JPanel navMenu = new JPanel();
        navMenu.setLayout(new BoxLayout(navMenu, BoxLayout.Y_AXIS));
        navMenu.setOpaque(false);
        navMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        navMenu.add(createNavButton("Dashboard", e -> navigateTo("DASHBOARD")));
        navMenu.add(Box.createVerticalStrut(8));
        navMenu.add(createNavButton("Historical Analysis", e -> navigateTo("HISTORICAL")));
        navMenu.add(Box.createVerticalStrut(8));
        navMenu.add(createNavButton("Compare Cities", e -> navigateTo("COMPARE")));
        navMenu.add(Box.createVerticalStrut(8));
        navMenu.add(createNavButton("Search History", e -> navigateTo("HISTORY")));
        navMenu.add(Box.createVerticalStrut(8));
        navMenu.add(createNavButton("Export Report", e -> exportReportAction()));
        navMenu.add(Box.createVerticalStrut(8));
        navMenu.add(createNavButton("About Project", e -> navigateTo("ABOUT")));

        sidebar.add(brandPanel, BorderLayout.NORTH);
        sidebar.add(navMenu, BorderLayout.CENTER);

        return sidebar;
    }

    private JButton createNavButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setFont(Utils.FONT_BODY);
        btn.setForeground(Utils.COLOR_TEXT_PRIMARY);
        btn.setBackground(Utils.COLOR_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Utils.COLOR_CARD_BG);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Utils.COLOR_SIDEBAR);
            }
        });

        btn.addActionListener(listener);
        return btn;
    }

    private JPanel createTopNavPanel() {
        JPanel topNav = new JPanel(new BorderLayout());
        topNav.setPreferredSize(new Dimension(0, 70));
        topNav.setBackground(Utils.COLOR_SIDEBAR);
        topNav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Utils.COLOR_BORDER));
        topNav.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblCurrentCityHeader = Utils.createStyledLabel("City: Loading...", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);
        lblDateTimeHeader = Utils.createStyledLabel(Utils.getFormattedDate(), Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY);

        JPanel leftHeader = new JPanel(new GridLayout(2, 1));
        leftHeader.setOpaque(false);
        leftHeader.add(lblCurrentCityHeader);
        leftHeader.add(lblDateTimeHeader);

        // Modern Search Controls
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);

        txtSearch = Utils.createStyledTextField(18);
        txtSearch.setPreferredSize(new Dimension(220, 36));
        txtSearch.setText("Mumbai");

        JButton btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(90, 36));
        btnSearch.setFont(Utils.FONT_BODY);
        btnSearch.setBackground(Utils.COLOR_ACCENT);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> fetchCityWeather(txtSearch.getText().trim()));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setPreferredSize(new Dimension(90, 36));
        btnRefresh.setFont(Utils.FONT_BODY);
        btnRefresh.setBackground(Utils.COLOR_CARD_BG);
        btnRefresh.setForeground(Utils.COLOR_TEXT_PRIMARY);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> {
            if (currentData != null) fetchCityWeather(currentData.getCityName());
        });

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);

        topNav.add(leftHeader, BorderLayout.WEST);
        topNav.add(searchPanel, BorderLayout.EAST);

        return topNav;
    }

    private JPanel createDashboardViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel centerGrid = new JPanel(new GridLayout(1, 2, 15, 15));
        centerGrid.setOpaque(false);

        // Metrics Overview Card
        JPanel cardMetrics = Utils.createCardPanel();
        cardMetrics.setLayout(new GridLayout(6, 2, 10, 10));

        lblTemp = Utils.createStyledLabel("-- °C", Utils.FONT_TITLE, Utils.COLOR_ACCENT);
        lblFeelsLike = Utils.createStyledLabel("Feels Like: --", Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY);
        lblCondition = Utils.createStyledLabel("Condition: --", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);
        lblHumidity = Utils.createStyledLabel("Humidity: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblPressure = Utils.createStyledLabel("Pressure: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblWind = Utils.createStyledLabel("Wind: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblVisibility = Utils.createStyledLabel("Visibility: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblUv = Utils.createStyledLabel("UV Index: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblSunrise = Utils.createStyledLabel("Sunrise: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
        lblSunset = Utils.createStyledLabel("Sunset: --", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);

        cardMetrics.add(lblTemp); cardMetrics.add(lblCondition);
        cardMetrics.add(lblFeelsLike); cardMetrics.add(lblHumidity);
        cardMetrics.add(lblPressure); cardMetrics.add(lblWind);
        cardMetrics.add(lblVisibility); cardMetrics.add(lblUv);
        cardMetrics.add(lblSunrise); cardMetrics.add(lblSunset);

        // Comfort Score Card
        JPanel cardComfort = Utils.createCardPanel();
        cardComfort.setLayout(new BorderLayout(10, 10));

        JLabel lblComfortHeader = Utils.createStyledLabel("Weather Comfort Score", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);
        lblComfortScore = Utils.createStyledLabel("-- / 100", new Font("Segoe UI", Font.BOLD, 42), Utils.COLOR_ACCENT);
        lblComfortScore.setHorizontalAlignment(SwingConstants.CENTER);

        lblComfortRating = Utils.createStyledLabel("Rating: --", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);
        lblComfortRating.setHorizontalAlignment(SwingConstants.CENTER);

        lblComfortDesc = Utils.createStyledLabel("Search a location to calculate comfort score.", Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY);
        lblComfortDesc.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel comfortCenter = new JPanel(new GridLayout(3, 1));
        comfortCenter.setOpaque(false);
        comfortCenter.add(lblComfortScore);
        comfortCenter.add(lblComfortRating);
        comfortCenter.add(lblComfortDesc);

        cardComfort.add(lblComfortHeader, BorderLayout.NORTH);
        cardComfort.add(comfortCenter, BorderLayout.CENTER);

        centerGrid.add(cardMetrics);
        centerGrid.add(cardComfort);

        // Insights Container
        JPanel cardInsights = Utils.createCardPanel();
        cardInsights.setLayout(new BorderLayout(10, 10));
        JLabel lblInsightsTitle = Utils.createStyledLabel("Smart Environmental Insights", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);

        panelInsightsContainer = new JPanel();
        panelInsightsContainer.setLayout(new BoxLayout(panelInsightsContainer, BoxLayout.Y_AXIS));
        panelInsightsContainer.setOpaque(false);

        cardInsights.add(lblInsightsTitle, BorderLayout.NORTH);
        cardInsights.add(panelInsightsContainer, BorderLayout.CENTER);

        panel.add(centerGrid, BorderLayout.CENTER);
        panel.add(cardInsights, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHistoryViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = Utils.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = Utils.createStyledLabel("Recent Search History", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> historyJList = new JList<>(listModel);
        historyJList.setBackground(Utils.COLOR_CARD_BG);
        historyJList.setForeground(Utils.COLOR_TEXT_PRIMARY);
        historyJList.setFont(Utils.FONT_BODY);

        JButton btnClear = new JButton("Clear Search History");
        btnClear.setBackground(new Color(192, 57, 43));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);

        btnClear.addActionListener(e -> {
            historyManager.clearHistory();
            listModel.clear();
        });

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(new JScrollPane(historyJList), BorderLayout.CENTER);
        card.add(btnClear, BorderLayout.SOUTH);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAboutViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = Utils.createCardPanel();
        card.setLayout(new GridLayout(8, 1, 10, 10));

        card.add(Utils.createStyledLabel("ATMOS — Weather Intelligence & Analytics", Utils.FONT_TITLE, Utils.COLOR_ACCENT));
        card.add(Utils.createStyledLabel("Objective: Advanced environmental data analysis platform built for desktop platforms.", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY));
        card.add(Utils.createStyledLabel("Tech Stack: Java 17+, Swing GUI, JFreeChart, Open-Meteo REST API", Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY));
        card.add(Utils.createStyledLabel("OOP Principles Implemented:", Utils.FONT_HEADER, Utils.COLOR_TEXT_PRIMARY));
        card.add(Utils.createStyledLabel(" • Encapsulation, Polymorphism, Inheritance, Interfaces", Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY));
        card.add(Utils.createStyledLabel(" • Method Overloading/Overriding, Exception Handling, File I/O", Utils.FONT_BODY, Utils.COLOR_TEXT_SECONDARY));
        card.add(Utils.createStyledLabel("Developer: Second Year Engineering Microproject", Utils.FONT_HEADER, Utils.COLOR_ACCENT));

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private void fetchCityWeather(String city) {
        if (city == null || city.trim().isEmpty()) return;

        SwingWorker<WeatherData, Void> worker = new SwingWorker<>() {
            @Override
            protected WeatherData doInBackground() throws Exception {
                return WeatherAPI.fetchWeatherData(city);
            }

            @Override
            protected void done() {
                try {
                    currentData = get();
                    historyManager.addSearch(currentData.getCityName());
                    updateDashboardUI(currentData);
                    graphPanel.updateGraphData(currentData);
                    refreshHistoryListView();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Dashboard.this,
                            "Could not retrieve weather data: " + e.getMessage(),
                            "Fetch Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void updateDashboardUI(WeatherData data) {
        lblCurrentCityHeader.setText("City: " + data.getCityName());
        lblDateTimeHeader.setText(Utils.getFormattedDate() + " | " + Utils.getFormattedTime());

        lblTemp.setText(String.format("%.1f °C", data.getTemperature()));
        lblFeelsLike.setText(String.format("Feels Like: %.1f °C", data.getApparentTemperature()));
        lblCondition.setText(data.getWeatherCondition());
        lblHumidity.setText(String.format("Humidity: %d %%", data.getHumidity()));
        lblPressure.setText(String.format("Pressure: %.1f hPa", data.getPressure()));
        lblWind.setText(String.format("Wind Speed: %.1f km/h", data.getWindSpeed()));
        lblVisibility.setText(String.format("Visibility: %.1f km", data.getVisibility()));
        lblUv.setText(String.format("UV Index: %d", data.getUvIndex()));
        lblSunrise.setText(String.format("Sunrise: %s", data.getSunrise()));
        lblSunset.setText(String.format("Sunset: %s", data.getSunset()));

        WeatherAnalyzer.ComfortReport report = WeatherAnalyzer.calculateComfortScore(data);
        lblComfortScore.setText(report.getScore() + " / 100");
        lblComfortRating.setText("Rating: " + report.getRating());
        lblComfortDesc.setText("<html><center>" + report.getExplanation() + "</center></html>");

        panelInsightsContainer.removeAll();
        List<String> insights = WeatherAnalyzer.generateInsights(data);
        for (String insight : insights) {
            JLabel lbl = Utils.createStyledLabel("• " + insight, Utils.FONT_BODY, Utils.COLOR_TEXT_PRIMARY);
            lbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            panelInsightsContainer.add(lbl);
        }
        panelInsightsContainer.revalidate();
        panelInsightsContainer.repaint();
    }

    private void refreshHistoryListView() {
        if (historyViewPanel == null) return;
        List<String> history = historyManager.getHistory();
        Component[] comps = historyViewPanel.getComponents();
        if (comps.length > 0 && comps[0] instanceof JPanel card) {
            for (Component c : card.getComponents()) {
                if (c instanceof JScrollPane sp) {
                    if (sp.getViewport().getView() instanceof JList<?> jl) {
                        @SuppressWarnings("unchecked")
                        JList<String> list = (JList<String>) jl;
                        DefaultListModel<String> model = new DefaultListModel<>();
                        for (String item : history) model.addElement(item);
                        list.setModel(model);
                    }
                }
            }
        }
    }

    private void exportReportAction() {
        if (currentData == null) {
            JOptionPane.showMessageDialog(this, "No weather data loaded to export.", "Export Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(currentData.getCityName().toLowerCase() + "_weather_report.txt"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            WeatherAnalyzer.ComfortReport report = WeatherAnalyzer.calculateComfortScore(currentData);
            List<String> insights = WeatherAnalyzer.generateInsights(currentData);

            boolean success = FileManager.exportWeatherReport(
                    fileToSave.getAbsolutePath(), currentData, report, insights);

            if (success) {
                JOptionPane.showMessageDialog(this, "Weather Report successfully exported!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to write report file.", "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void navigateTo(String cardName) {
        cardLayout.show(mainContentPanel, cardName);
    }
}