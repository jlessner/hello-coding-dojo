package thementag;

/**
 * Hilfsdatenklasse zum Speichern der einzelnen Zeitkomponenten.
 */
public class TimeComponents {
    private final long hours;
    private final long minutes;
    private final long seconds;

    /**
     * Erstellt eine neue Instanz der Zeitkomponenten aus einer Gesamtzeit in Sekunden.
     * Berechnet automatisch Stunden, Minuten und verbleibende Sekunden.
     *
     * @param totalSeconds Die Gesamtzeit in Sekunden
     */
    public TimeComponents(long totalSeconds) {
        this.hours = totalSeconds / 3600;
        long remainingSeconds = totalSeconds % 3600;
        this.minutes = remainingSeconds / 60;
        this.seconds = remainingSeconds % 60;
    }

    /**
     * @return Die Anzahl der Stunden
     */
    public long getHours() {
        return hours;
    }

    /**
     * @return Die Anzahl der Minuten
     */
    public long getMinutes() {
        return minutes;
    }

    /**
     * @return Die Anzahl der Sekunden
     */
    public long getSeconds() {
        return seconds;
    }
}
