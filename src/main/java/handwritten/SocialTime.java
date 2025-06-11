package handwritten;

import java.util.Arrays;
import java.util.stream.Collectors;

class SocialTime {

  public String socialTime(int ageInSeconds) {
    Age age = new Age(ageInSeconds);
    return commaSeparated(
      hoursPortion(age),
      minutesPortion(age),
      secondsPortion(age)
    );
  }

  private String commaSeparated(String... portions) {
    return Arrays.stream(portions)
      .filter(portion -> portion != null)
      .collect(Collectors.joining(", "));
  }

  private String hoursPortion(Age age) {
    return singularOrPlural(age.hours(), "hour");
  }

  private String minutesPortion(Age age) {
    return singularOrPlural(age.minutes(), "minute");
  }

  private String secondsPortion(Age age) {
    if (age.hasHours()) {
      return null;
    }
    if (age.hasMinutes() && age.hasFewSeconds()) {
      return null;
    }
    String amountString = age.hasFewSeconds() ? "few" : "" + age.seconds();
    return amountString + " seconds";
  }

  private String singularOrPlural(int portionAmount, String unit) {
    if (portionAmount == 0) {
      return null;
    }
    String unitString = (portionAmount > 1) ? unit + "s" : unit;
    return portionAmount + " " + unitString;
  }

}
