/**
 * Utility class for terminal colors using ANSI escape codes
 */
public class ColorUtil {
    // ANSI color codes
    public static final String RESET = "\033[0m";
    
    // Regular colors
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String MAGENTA = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";
    
    // Bold colors
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String MAGENTA_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String WHITE_BOLD = "\033[1;37m";
    
    // Background colors
    public static final String BLACK_BACKGROUND = "\033[40m";
    public static final String RED_BACKGROUND = "\033[41m";
    public static final String GREEN_BACKGROUND = "\033[42m";
    public static final String YELLOW_BACKGROUND = "\033[43m";
    public static final String BLUE_BACKGROUND = "\033[44m";
    public static final String MAGENTA_BACKGROUND = "\033[45m";
    public static final String CYAN_BACKGROUND = "\033[46m";
    public static final String WHITE_BACKGROUND = "\033[47m";
    
    /**
     * Color a text with the specified color
     */
    public static String colorize(String text, String color) {
        return color + text + RESET;
    }
    
    /**
     * Success message (green)
     */
    public static String success(String text) {
        return colorize("✓ " + text, GREEN_BOLD);
    }
    
    /**
     * Error message (red)
     */
    public static String error(String text) {
        return colorize("✗ " + text, RED_BOLD);
    }
    
    /**
     * Warning message (yellow)
     */
    public static String warning(String text) {
        return colorize("⚠ " + text, YELLOW_BOLD);
    }
    
    /**
     * Info message (cyan)
     */
    public static String info(String text) {
        return colorize(text, CYAN);
    }
    
    /**
     * Header message (bold cyan)
     */
    public static String header(String text) {
        return colorize(text, CYAN_BOLD);
    }
    
    /**
     * Title message (bold blue)
     */
    public static String title(String text) {
        return colorize(text, BLUE_BOLD);
    }
    
    /**
     * Highlight text (bold white)
     */
    public static String highlight(String text) {
        return colorize(text, WHITE_BOLD);
    }
}
