package thementag;

public class SocialTime {

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

    if (ageInSeconds < 10) {
      return "few seconds";
    } else if (ageInSeconds < 60) {
      return ageInSeconds + " seconds";
    } else {
      int minutes = ageInSeconds / 60;
      int seconds = ageInSeconds % 60;
      if (seconds == 0) {
        return minutes + " minute(s)";
      } else {
        return minutes + " minute(s), " + seconds + " seconds";
      }
    }
  }
}
