package rostock;

public class Age {
    private final int seconds;

    public Age(int seconds) {
        this.seconds = seconds;
    }

    public AgeSize getAgeSize() {
        if (seconds < 10) {
            return AgeSize.FEW_SECONDS;
        }

        if (seconds >= 60) {
            return AgeSize.MINUTES;
        }

        return AgeSize.SECONDS;
    }

    public int getMinutes() {
        return seconds / 60;
    }

    public int getSeconds() {
        return seconds % 60;
    }
}
