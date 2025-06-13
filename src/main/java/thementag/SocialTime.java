package thementag;

public class SocialTime {

  private static final int THRESHOLD_FEW_SECONDS = 10;
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int THRESHOLD_SECONDS_DISPLAY_WITH_MINUTES = 10;

  /**
   * Convert seconds into string "## seconds" with following rules:
   * - Periods under 10 seconds are represented as "few seconds".
   * - Periods under 60 seconds are represented as "XX seconds".
   * - If the input is negative, an IllegalArgumentException is thrown.
   * - Periods of a minute or more "XX minute(s), YY seconds"
   * - In case of minutes with only a few seconds rest, omit the seconds part.
   * - E.g. instead of "1minute(s), 5 seconds" just "1 minute(s)".
   *
   * @param ageInSeconds the age in seconds
   * @return a string representing the social time
   * @throws IllegalArgumentException if ageInSeconds is negative
   */
  public String socialTime(int ageInSeconds) {
    if (ageInSeconds < 0) {
      throw new IllegalArgumentException("Age in seconds cannot be negative");
    }

    if (ageInSeconds < THRESHOLD_FEW_SECONDS) {
      return formatFewSeconds();
    } else if (ageInSeconds < SECONDS_PER_MINUTE) {
      return formatSecondsOnly(ageInSeconds);
    } else {
      return formatMinutesAndSeconds(ageInSeconds);
    }
  }

  private String formatFewSeconds() {
    return "few seconds";
  }

  private String formatSecondsOnly(int seconds) {
    return String.format("%d seconds", seconds);
  }

  private String formatMinutesAndSeconds(int totalSeconds) {
    int minutes = totalSeconds / SECONDS_PER_MINUTE;
    int remainingSeconds = totalSeconds % SECONDS_PER_MINUTE;

    boolean displaySeconds = remainingSeconds >= THRESHOLD_SECONDS_DISPLAY_WITH_MINUTES;

    if (displaySeconds) {
      return String.format("%d minute(s), %d seconds", minutes, remainingSeconds);
    } else {
      return String.format("%d minute(s)", minutes);
    }
  }
}
