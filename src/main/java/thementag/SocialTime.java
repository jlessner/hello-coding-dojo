package thementag;

/**
 * Formats time durations into human-friendly text representations.
 */
public class SocialTime {

    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int FEW_SECONDS_THRESHOLD = 10;

    private static final String FEW_SECONDS_TEXT = "few seconds";
    private static final String SECOND_UNIT = "second";
    private static final String MINUTE_UNIT = "minute";
    private static final String HOUR_UNIT = "hour";
    private static final String TIME_SEPARATOR = ", ";

    /**
     * Formats a time value given in seconds into a user-friendly textual representation.
     *
     * This method converts seconds into a readable representation. For less than 10 seconds,
     * it returns "few seconds". Otherwise, it converts the time into an appropriate format
     * with hours, minutes and/or seconds, with words correctly displayed in singular or plural form.
     *
     * @param seconds The time to format in seconds
     * @return A user-friendly text representation of the time
     */
    String formatTime(int seconds) {
        if (seconds < FEW_SECONDS_THRESHOLD) {
            return FEW_SECONDS_TEXT;
        }

        TimeComponents components = decomposeTime(seconds);
        return buildTimeText(components);
    }

    /**
     * Decomposes seconds into hours, minutes, and remaining seconds.
     */
    private TimeComponents decomposeTime(int totalSeconds) {
        int hours = 0;
        int minutes = 0;
        int seconds = totalSeconds;

        if (seconds >= SECONDS_IN_MINUTE) {
            minutes = seconds / SECONDS_IN_MINUTE;
            seconds = seconds % SECONDS_IN_MINUTE;

            if (minutes >= MINUTES_IN_HOUR) {
                hours = minutes / MINUTES_IN_HOUR;
                minutes = minutes % MINUTES_IN_HOUR;
            }
        }

        return new TimeComponents(hours, minutes, seconds);
    }

    /**
     * Builds a human-friendly time text from the given time components.
     */
    private String buildTimeText(TimeComponents components) {
        StringBuilder builder = new StringBuilder();

        if (components.hasHours()) {
            builder.append(createUnitText(components.getHours(), HOUR_UNIT));
        }

        if (components.hasMinutes()) {
            if (builder.length() > 0) {
                builder.append(TIME_SEPARATOR);
            }
            builder.append(createUnitText(components.getMinutes(), MINUTE_UNIT));
        }

        if (components.hasSignificantSeconds()) {
            if (builder.length() > 0) {
                builder.append(TIME_SEPARATOR);
            }
            builder.append(createUnitText(components.getSeconds(), SECOND_UNIT));
        }

        // If no components were added, just use seconds (edge case)
        if (builder.length() == 0) {
            builder.append(createUnitText(components.getSeconds(), SECOND_UNIT));
        }

        return builder.toString();
    }

    /**
     * Creates a text representation for a time unit (e.g., "5 hours").
     */
    private String createUnitText(int count, String unit) {
        return count + " " + pluralize(count, unit);
    }

    /**
     * Returns singular or plural form of a word based on count.
     */
    private String pluralize(int count, String word) {
        return count == 1 ? word : word + "s";
    }

    /**
     * Represents decomposed time components (hours, minutes, seconds).
     */
    private class TimeComponents {
        private final int hours;
        private final int minutes;
        private final int seconds;

        public TimeComponents(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public int getHours() {
            return hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public int getSeconds() {
            return seconds;
        }

        public boolean hasHours() {
            return hours > 0;
        }

        public boolean hasMinutes() {
            return minutes > 0;
        }

        public boolean hasSignificantSeconds() {
            return seconds >= FEW_SECONDS_THRESHOLD;
        }
    }
}