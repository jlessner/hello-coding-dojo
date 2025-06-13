package thementag;

public class SocialTime {

  public String socialTime(int ageInSeconds) {
    if (ageInSeconds < 0) {
      throw new IllegalArgumentException("Age must be a positive integer");
    }
    if (ageInSeconds < 10) {
      return "few seconds";
    }
    return ageInSeconds + " seconds";
  }
}
