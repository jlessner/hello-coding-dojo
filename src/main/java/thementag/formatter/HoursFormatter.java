package thementag.formatter;

/**
 * Formatter für Stundenangaben.
 */
public class HoursFormatter implements TimeFormatter {

    @Override
    public String format(long hours) {
        if (hours == 0) {
            return "";
        } else if (hours == 1) {
            return "1 hour";
        } else {
            return hours + " hours";
        }
    }
}
