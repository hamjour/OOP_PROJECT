package utils;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Utils {
    // Main Colors from your palette
    public static final Color DUST_GREY = Color.decode("#DAD7CD");    // Light beige/grey
    public static final Color DRY_SAGE = Color.decode("#A3B18A");     // Light sage green
    public static final Color FERN = Color.decode("#588157");         // Medium green
    public static final Color HUNTER_GREEN = Color.decode("#3A5A40"); // Dark green
    public static final Color PINE_TEAL = Color.decode("#344E41");    // Darkest green

    // Recommended usage for UI:
    public static final Color BG_PRIMARY = PINE_TEAL;      // Main background
    public static final Color BG_SECONDARY = HUNTER_GREEN;  // Secondary background
    public static final Color ACCENT = FERN;                // Buttons, highlights
    public static final Color TEXT_PRIMARY = DUST_GREY;     // Main text
    public static final Color TEXT_SECONDARY = DRY_SAGE;    // Secondary text/placeholders

    public static EmptyBorder addPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}