package thementag;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SocialTimeTest {

    @Test
    void testFewSeconds() {
        SocialTime socialTime = new SocialTime();
        assertEquals("few seconds", socialTime.formatTime(0));
    }

    @Test
    void testExactTenSeconds() {
        SocialTime socialTime = new SocialTime();
        assertEquals("10 seconds", socialTime.formatTime(10));
    }

    @Test
    void testMoreThanTenSeconds() {
        SocialTime socialTime = new SocialTime();
        assertEquals("15 seconds", socialTime.formatTime(15));
    }

    @Test
    void testExactMinute() {
        SocialTime socialTime = new SocialTime();
        assertEquals("1 minute", socialTime.formatTime(60));
    }

    @Test
    void testMinuteWithFewRestSeconds() {
        SocialTime socialTime = new SocialTime();
        assertEquals("1 minute", socialTime.formatTime(65)); // Rest < 10
    }

    @Test
    void testMinuteWithRestSeconds() {
        SocialTime socialTime = new SocialTime();
        assertEquals("1 minute, 12 seconds", socialTime.formatTime(72)); // Rest >= 10
    }

    @Test
    void testMultipleMinutes() {
        SocialTime socialTime = new SocialTime();
        assertEquals("2 minutes", socialTime.formatTime(125)); // Rest < 10
    }

    @Test
    void testMultipleMinutesWithRestSeconds() {
        SocialTime socialTime = new SocialTime();
        assertEquals("2 minutes, 15 seconds", socialTime.formatTime(135)); // Rest >= 10
    }
    @Test
    void testMultipleHours() {
        SocialTime socialTime = new SocialTime();
        assertEquals("1 hour(s), 1 minute", socialTime.formatTime(3660)); // 1 hour, 5 minutes
    }
    @Test
    void testMultipleHoursWithRestMinutes() {
        SocialTime socialTime = new SocialTime();
        assertEquals("1 hour(s), 2 minutes", socialTime.formatTime(3720)); // 1 hour, 2 minutes
    }

}