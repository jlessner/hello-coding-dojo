package rostock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocialTimeTest {
  private SocialTime socialTime;

  public static Stream<Arguments> testMinutesProvider() {
    return Stream.of(
      Arguments.of(60, "1 minute(s), 0 second(s)"),
      Arguments.of(61, "1 minute(s), 1 second(s)")
    );
  }

  @BeforeEach
  void setUp() {
    socialTime = new SocialTime();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 5, 9})
  void testFewSeconds(int seconds) {
    assertEquals("few seconds", socialTime.socialTime(seconds));
  }

  @ParameterizedTest
  @MethodSource("testMinutesProvider")
  void testMinutes(int seconds, String expectedResult) {
    assertEquals(expectedResult, socialTime.socialTime(seconds));
  }
}
