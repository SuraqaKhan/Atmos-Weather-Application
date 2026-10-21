package com.atmos;

import javax.swing.*;

/**
 * Main application entry point for the ATMOS Weather Intelligence Platform.
 */
public class Main {
    public static void main(String[] args) {
        // Set System Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch Application on Swing Thread
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}