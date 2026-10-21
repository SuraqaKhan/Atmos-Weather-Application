package com.atmos;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class providing color palettes, typography specs, and custom Swing card components.
 */
public class Utils {
    // Dark Theme Design Tokens
    public static final Color COLOR_BG_DARK = new Color(18, 22, 28);
    public static final Color COLOR_SIDEBAR = new Color(24, 30, 38);
    public static final Color COLOR_CARD_BG = new Color(32, 40, 50);
    public static final Color COLOR_ACCENT = new Color(41, 128, 185);
    public static final Color COLOR_TEXT_PRIMARY = new Color(240, 243, 246);
    public static final Color COLOR_TEXT_SECONDARY = new Color(150, 165, 180);
    public static final Color COLOR_BORDER = new Color(45, 55, 68);
    public static final Color COLOR_INPUT_BG = new Color(24, 30, 38);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static String getFormattedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"));
    }

    public static String getFormattedTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }

    public static JPanel createCardPanel() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return card;
    }

    public static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    // Modern Dark Input Field Helper
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(COLOR_INPUT_BG);
        field.setForeground(COLOR_TEXT_PRIMARY);
        field.setCaretColor(COLOR_TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMargin(new Insets(6, 10, 6, 10));

        Border lineBorder = BorderFactory.createLineBorder(COLOR_BORDER, 1, true);
        Border padding = new EmptyBorder(4, 8, 4, 8);
        field.setBorder(new CompoundBorder(lineBorder, padding));

        return field;
    }
}