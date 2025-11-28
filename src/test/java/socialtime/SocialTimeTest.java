package socialtime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SocialTimeTest {

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9})
  void testFewSeconds(int seconds) {
    String time = new SocialTime().socialTime(seconds);
    assertEquals("few seconds", time);
  }

  @ParameterizedTest
  @CsvSource({
      "10, 10 seconds",
      "11, 11 seconds",
      "12, 12 seconds",
      "59, 59 seconds"
  })
  void testSeconds(int seconds, String expected) {
    String time = new SocialTime().socialTime(seconds);
    assertEquals(expected, time);
  }

  @ParameterizedTest
  @CsvSource({
      "60, 1 minute",
      "61, 1 minute",
      "62, 1 minute",
      "119, '1 minute, 59 seconds'",
      "120, 2 minutes",
      "121, 2 minutes"
  })
  void testMinutesAndSeconds(int seconds, String expected) {
    String time = new SocialTime().socialTime(seconds);
    assertEquals(expected, time);
  }

  @Test
  void testNegativeSeconds() {
    assertThrows(IllegalArgumentException.class, () -> new SocialTime().socialTime(-1));
  }
}