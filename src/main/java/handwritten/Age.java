package handwritten;

class Age {
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int MINUTES_PER_HOUR = 60;
  private static final int SECONDS_PER_HOUR = 60 * 60;
  private static final int FEW_SECONDS = 10;

  private int ageInSeconds;

  public Age(int ageInSeconds) {
    this.ageInSeconds = ageInSeconds;
  }

  public boolean hasHours() { return hours() > 0; }

  public int hours() { return ageInSeconds / SECONDS_PER_HOUR; }

  public boolean hasMinutes() { return minutes() > 0; }

  public int minutes() { return ageInSeconds % SECONDS_PER_HOUR / MINUTES_PER_HOUR; }

  public int seconds() { return ageInSeconds % SECONDS_PER_MINUTE; }

  public boolean hasFewSeconds() {
    return seconds() < FEW_SECONDS;
  }
}
