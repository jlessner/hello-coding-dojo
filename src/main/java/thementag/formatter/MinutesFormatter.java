package thementag.formatter;

/**
 * Formatter für Minutenangaben.
 */
public class MinutesFormatter implements TimeFormatter {

    @Override
    public String format(long minutes) {
        if (minutes == 0) {
            return "";
        } else if (minutes == 1) {
            return "1 minute";
        } else {
            return minutes + " minutes";
        }
    }
}
