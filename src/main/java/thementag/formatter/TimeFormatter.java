package thementag.formatter;

/**
 * Interface für Zeitformatierungen.
 */
public interface TimeFormatter {

    /**
     * Formatiert einen Zeitwert.
     *
     * @param value Der zu formatierende Wert
     * @return Eine formatierte Zeichenkette oder einen leeren String, wenn nicht anwendbar
     */
    String format(long value);
}
