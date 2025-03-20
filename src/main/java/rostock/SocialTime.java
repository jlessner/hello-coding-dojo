package rostock;

public class SocialTime {
  public String socialTime(int ageInSeconds) {

    Age age = new Age(ageInSeconds);

    return switch (age.getAgeSize()){
      case FEW_SECONDS -> "few seconds";
      case SECONDS -> ageInSeconds + " seconds";
      case MINUTES -> printMinutes(age);
    };
  }

  private static String printMinutes(Age age) {
    return age.getMinutes() + " minute(s), " + age.getSeconds() + " second(s)";
  }
}