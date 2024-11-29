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

	@Test
	void renderFewMoreSeconds() {
		SocialTime socialTime = new SocialTime();
		String stringFewMoreSeconds = socialTime.render(11);
		assertEquals("11 seconds",stringFewMoreSeconds);
	}

	@Test
	void renderMinutesAndSeconds() {
		SocialTime socialTime = new SocialTime();
		String stringMinutesAndSeconds = socialTime.render(60);
		assertEquals("1 minute(s), 0 seconds",stringMinutesAndSeconds);
	}

	@Test
	void renderMinutesAndSeconds2() {
		SocialTime socialTime = new SocialTime();
		String stringMinutesAndSeconds = socialTime.render(69);
		assertEquals("1 minute(s), 9 seconds",stringMinutesAndSeconds);
	}


}