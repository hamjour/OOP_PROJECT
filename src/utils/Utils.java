package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class Utils {
    public static final Color DUST_GREY = Color.decode("#DAD7CD");
    public static final Color DRY_SAGE = Color.decode("#A3B18A");
    public static final Color FERN = Color.decode("#588157");
    public static final Color HUNTER_GREEN = Color.decode("#3A5A40");
    public static final Color PINE_TEAL = Color.decode("#344E41");

    public static final Color BG_PRIMARY = PINE_TEAL;      // Main background
    public static final Color BG_SECONDARY = HUNTER_GREEN;  // Secondary background
    public static final Color ACCENT = FERN;                // Buttons, highlights
    public static final Color TEXT_PRIMARY = DUST_GREY;     // Main text
    public static final Color TEXT_SECONDARY = DRY_SAGE;    // Secondary text/placeholders

    // Static initializer: enable anti-aliased text and set system look-and-feel + default font
    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        FontUIResource baseFont = new FontUIResource("SansSerif", Font.PLAIN, 13);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, baseFont);
            }
        }

        UIManager.put("Table.selectionBackground", ACCENT);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", BG_SECONDARY);
        UIManager.put("Button.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.background", BG_SECONDARY);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.background", BG_SECONDARY);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
    }

    public static EmptyBorder addPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}