package thementag;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocialTimeTest {

  public static Stream<Arguments> testSocialTime_params() {
    return Stream.of(
      Arguments.of(5, "few seconds"),
      Arguments.of(10, "10 seconds"),
      Arguments.of(61, "1 minute"),
      Arguments.of(70, "1 minute, 10 seconds"),
      Arguments.of(129, "2 minutes"),
      Arguments.of(131, "2 minutes, 11 seconds")
    );
  }

  @ParameterizedTest
  @MethodSource("testSocialTime_params")
  void testSocialTime(int ageInSeconds, String expectedOutput) {
    assertEquals(expectedOutput, new SocialTime().socialTime(ageInSeconds));
  }

}
