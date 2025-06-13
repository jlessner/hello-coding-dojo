package thementag.formatter;

/**
 * Formatter für Sekundenangaben unter Berücksichtigung des Gesamtkontexts.
 */
public class SecondsFormatter implements TimeFormatter {

    private final long hours;
    private final long minutes;

    /**
     * Erstellt einen neuen SecondsFormatter mit Kontext.
     *
     * @param hours Die Anzahl der Stunden für Kontextentscheidungen
     * @param minutes Die Anzahl der Minuten für Kontextentscheidungen
     */
    public SecondsFormatter(long hours, long minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    @Override
    public String format(long seconds) {
        // Wenn eine Stunde oder mehr vergangen ist, keine Sekunden anzeigen
        if (hours >= 1) {
            return "";
        }

        // Wenn eine Minute oder mehr vergangen ist und weniger als 10 Sekunden, keine Sekunden anzeigen
        if (minutes >= 1 && seconds < 10) {
            return "";
        }

        // Spezialfall für wenige Sekunden
        if (seconds < 10) {
            return "few seconds";
        } else {
            return seconds + " seconds";
        }
    }
}
