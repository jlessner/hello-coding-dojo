package rostock;

public class SocialTime {
  public String socialTime(int ageInSeconds) {
    if (ageInSeconds < 10) {
      return "few seconds";
    }
    return ageInSeconds + " seconds";
  }
}
