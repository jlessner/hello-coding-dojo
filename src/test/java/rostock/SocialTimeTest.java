package rostock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocialTimeTest {
  private SocialTime socialTime;

  @BeforeEach
  void setUp() {
    socialTime = new SocialTime();
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 5, 9})
  void testFewSeconds(int seconds) {
    assertEquals("few seconds", socialTime.socialTime(seconds));
  }
}
