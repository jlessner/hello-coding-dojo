package thementag;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Testklasse für die SocialTime-Implementierung.
 */
public class SocialTimeTest {

    private final SocialTime socialTime = new SocialTime();

    @Test
    @DisplayName("Sollte Exception bei negativer Zeitdauer werfen")
    public void testNegativeAgeShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                                         () -> socialTime.formatTimeAgo(-1));

        assertTrue(exception.getMessage().contains("negativ"));
    }

    @Test
    @DisplayName("Sollte 'few seconds' für sehr kurze Zeitdauern zurückgeben")
    public void testFewSeconds() {
        assertEquals("few seconds", socialTime.formatTimeAgo(5));
    }

    @Test
    @DisplayName("Sollte '20 seconds' für Zeitdauern zwischen 10 und 59 Sekunden zurückgeben")
    public void testLessThanOneMinute() {
        assertEquals("20 seconds", socialTime.formatTimeAgo(20));
        assertEquals("59 seconds", socialTime.formatTimeAgo(59));
    }

    @Test
    @DisplayName("Sollte '1 minute' für genau eine Minute zurückgeben")
    public void testExactlyOneMinute() {
        assertEquals("1 minute", socialTime.formatTimeAgo(60));
    }

    @Test
    @DisplayName("Sollte '2 minutes' für mehr als eine Minute zurückgeben")
    public void testMoreThanOneMinute() {
        assertEquals("2 minutes", socialTime.formatTimeAgo(120));
    }

    @Test
    @DisplayName("Sollte '1 hour' für genau eine Stunde zurückgeben")
    public void testExactlyOneHour() {
        assertEquals("1 hour", socialTime.formatTimeAgo(3600));
    }

    @Test
    @DisplayName("Sollte '2 hours' für genau zwei Stunden zurückgeben")
    public void testExactlyTwoHours() {
        assertEquals("2 hours", socialTime.formatTimeAgo(7200));
    }

    @Test
    @DisplayName("Sollte '1 hour, 30 minutes' für komplexe Zeitdauern zurückgeben")
    public void testComplexTime() {
        assertEquals("1 hour, 30 minutes", socialTime.formatTimeAgo(5400)); // 1h 30min
    }

    @Test
    @DisplayName("Sollte '2 hours, 15 minutes' für komplexe Zeitdauern zurückgeben")
    public void testAnotherComplexTime() {
        assertEquals("2 hours, 15 minutes", socialTime.formatTimeAgo(8100)); // 2h 15min
    }

    @Test
    @DisplayName("Sollte '59 minutes, 59 seconds' für fast eine Stunde zurückgeben")
    public void testAlmostOneHour() {
        assertEquals("59 minutes, 59 seconds", socialTime.formatTimeAgo(3599)); // 59min 59sec
    }

    @Test
    @DisplayName("Sollte '1 minute, 15 seconds' für eine Minute und mehr als 10 Sekunden zurückgeben")
    public void testOneMinuteAndMoreThanTenSeconds() {
        assertEquals("1 minute, 15 seconds", socialTime.formatTimeAgo(75)); // 1min 15sec
    }

    @Test
    @DisplayName("Sollte '1 minute' für eine Minute und weniger als 10 Sekunden zurückgeben")
    public void testOneMinuteAndLessThanTenSeconds() {
        assertEquals("1 minute", socialTime.formatTimeAgo(65)); // 1min 5sec - Sekunden werden weggelassen
    }
}
