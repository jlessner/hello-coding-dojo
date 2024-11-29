package socialtime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SocialTimeTest {

	@Test
	void renderFewSeconds() {
		SocialTime socialTime = new SocialTime();
		String stringFewSeconds = socialTime.render(1);
		assertEquals("few seconds",stringFewSeconds);
	}
}