package thementag;

public class SocialTime {
    String formatTime(int seconds) {

        if (seconds < 10) {
            return "few seconds";
        } else if (seconds >= 60) {
            int minutes = seconds / 60;
            if (minutes > 60) {
                int minutesRest = minutes % 60;
                String minutesRestText = minutesRest == 1 ? "1 minute" : minutesRest + " minutes";
                int hours = minutes / 60;
                String hoursText = hours + " hour(s)";
                return hoursText + ", " + minutesRestText;
            }
            String minutesText = minutes == 1 ? "1 minute" : minutes + " minutes";
            if (seconds % 60 < 10) {
                return minutesText;
            }

            return minutesText + ", " + seconds % 60 + " seconds";
        }

        return seconds + " seconds";
    }


}
