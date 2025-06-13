package thementag;

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
      "60, 1 minute(s)",
      "61, 1 minute(s)",
      "62, 1 minute(s)",
      "119, '1 minute(s), 59 seconds'",
      "120, 2 minute(s)",
      "121, 2 minute(s)"
  })
  void testMinutesAndSeconds(int seconds, String expected) {
    String time = new SocialTime().socialTime(seconds);
    assertEquals(expected, time);
  }

  @Test
  void testNegativeSeconds() {
    boolean exception = false;
    try {
      new SocialTime().socialTime(-11);
    } catch (IllegalArgumentException e) {
      exception = true;
    }
    assertTrue(exception);
  }
}