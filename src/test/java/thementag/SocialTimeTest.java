package thementag;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SocialTimeTest {

  @Test
  void testFewSeconds() {
    String time = new SocialTime().socialTime(5);
    assertEquals("few seconds", time);
  }

  @Test
  void test11Seconds() {
    String time = new SocialTime().socialTime(11);
    assertEquals("11 seconds", time);
  }
}