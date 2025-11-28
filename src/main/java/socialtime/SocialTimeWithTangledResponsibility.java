package socialtime;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/** Propper application of Integration Operation Segregation Principle
 * But applying the principle makes clear that the class also suffers
 * from concentrating too much responsibility. */
class SocialTimeWithTangledResponsibility {


  /******* I N T E G R A T I O N   L E V E L   M E T H O D S ***********/

  public String socialTime(int ageInSeconds) {
    checkForValidAge(ageInSeconds);
    return commaSeparated(
      hoursPortion(ageInSeconds),
      minutesPortion(ageInSeconds),
      secondsPortion(ageInSeconds)
    );
  }

  /******* O P E R A T I O N   L E V E L   M E T H O D S ***********/

  private String commaSeparated(String... portions) {
    return Arrays.stream(portions)
      .filter(Objects::nonNull)
      .collect(Collectors.joining(", "));
  }

  private String hoursPortion(int ageInSeconds) {
    return singularOrPlural(hours(ageInSeconds), "hour");
  }

  private String minutesPortion(int ageInSeconds) {
    return singularOrPlural(minutes(ageInSeconds), "minute");
  }

  private String secondsPortion(int ageInSeconds) {
    if (hasHours(ageInSeconds)) {
      return null;
    }
    if (hasMinutes(ageInSeconds) && hasFewSeconds(ageInSeconds)) {
      return null;
    }
    String amountString = hasFewSeconds(ageInSeconds) ? "few" : "" + seconds(ageInSeconds);
    return amountString + " seconds";
  }

  private String singularOrPlural(int portionAmount, String unit) {
    if (portionAmount == 0) {
      return null;
    }
    String unitString = (portionAmount > 1) ? unit + "s" : unit;
    return portionAmount + " " + unitString;
  }

  private void checkForValidAge(int ageInSeconds) {
    if (ageInSeconds < 0) {
      throw new IllegalArgumentException("ageInSeconds must not be negative");
    }
  }











  /******* R E S P O N S I B I L I T Y   V I O L A T I O N ***********/
  private static final int SECONDS_PER_MINUTE = 60;
  private static final int MINUTES_PER_HOUR = 60;
  private static final int SECONDS_PER_HOUR = 60 * 60;
  private static final int FEW_SECONDS = 10;

  private int minutes(int ageInSeconds) {
    return ageInSeconds % SECONDS_PER_HOUR / MINUTES_PER_HOUR;
  }

  private int hours(int ageInSeconds) {
    return ageInSeconds / SECONDS_PER_HOUR;
  }

  private int seconds(int ageInSeconds) {
    return ageInSeconds % SECONDS_PER_MINUTE;
  }

  private boolean hasFewSeconds(int ageInSeconds) {
    return seconds(ageInSeconds) < FEW_SECONDS;
  }

  private boolean hasMinutes(int ageInSeconds) {
    return minutes(ageInSeconds) > 0;
  }

  private boolean hasHours(int ageInSeconds) {
    return hours(ageInSeconds) > 0;
  }


}
