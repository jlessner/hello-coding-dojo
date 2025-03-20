package rostock;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SocialTimeTest {
  private SocialTime socialTime = new SocialTime();

  @Test
  void testFewSeconds() {
    assertEquals("few seconds", socialTime.socialTime(1));
    assertEquals("few seconds", socialTime.socialTime(9));
  }
}
