package thementag;

import thementag.formatter.HoursFormatter;
import thementag.formatter.MinutesFormatter;
import thementag.formatter.SecondsFormatter;
import thementag.formatter.TimeFormatter;

/**
 * Klasse zur Umwandlung einer Zeitdauer in Sekunden in ein benutzerfreundliches Format
 * für die Anzeige in einem Social-Media-System.
 */
public class SocialTime {

    private final TimeFormatter hoursFormatter;
    private final TimeFormatter minutesFormatter;

    /**
     * Erstellt eine neue SocialTime-Instanz mit den Standard-Formatierern.
     */
    public SocialTime() {
        this.hoursFormatter = new HoursFormatter();
        this.minutesFormatter = new MinutesFormatter();
    }

    /**
     * Wandelt eine Zeitdauer in Sekunden in einen lesbaren Text um.
     *
     * @param ageInSeconds Die Zeitdauer in Sekunden
     * @return Ein lesbarer Text, der die Zeitdauer darstellt
     * @throws IllegalArgumentException Wenn die Zeitdauer negativ ist
     */
    public String formatTimeAgo(long ageInSeconds) {
        if (ageInSeconds < 0) {
            throw new IllegalArgumentException("Die Zeitdauer darf nicht negativ sein");
        }

        // Verwende TimeComponents zur Berechnung der Stunden, Minuten und Sekunden
        TimeComponents components = new TimeComponents(ageInSeconds);

        // Verwende die Formatter, um die Zeitteile zu formatieren
        String hoursText = hoursFormatter.format(components.getHours());
        String minutesText = minutesFormatter.format(components.getMinutes());

        // Der SecondsFormatter benötigt den Kontext von Stunden und Minuten
        TimeFormatter secondsFormatter = new SecondsFormatter(components.getHours(), components.getMinutes());
        String secondsText = secondsFormatter.format(components.getSeconds());

        // Zusammenbau des Ergebnisses mit der bestehenden joinParts-Methode
        return joinParts(hoursText, minutesText, secondsText);
    }

    /**
     * Fügt alle nicht leeren Teile zusammen.
     */
    private String joinParts(String... parts) {
        StringBuilder result = new StringBuilder();

        boolean first = true;
        for (String part : parts) {
            if (part != null && !part.isEmpty()) {
                if (!first) {
                    result.append(", ");
                }
                result.append(part);
                first = false;
            }
        }

        return result.toString();
    }
}
