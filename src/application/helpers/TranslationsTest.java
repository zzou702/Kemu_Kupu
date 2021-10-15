package application.helpers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Unit tests for the Answer class */
class TranslationsTest {

	@Test
	void testGetBasicMessage() {
		assertEquals(Translations.getInstance().get("newGame", "en"), "Start Game");
		assertEquals(Translations.getInstance().get("newGame", "mi"), "Tāti Kēmu");
		assertEquals(
			Translations.getInstance().get("newGame", "both"),
			"Tāti Kēmu\n(Start Game)"
		);
	}

	@Test
	void testInvalidMessageId() {
		assertEquals(
			Translations.getInstance().get("invalidddddd", "en"),
			"MISSING TRANSLATION FOR invalidddddd"
		);
	}

	@Test
	void testWithMessageFormat() {
		assertEquals(
			Translations.getInstance().get("time", "mi", "1:52"),
			"Tāima: 1:52"
		);
	}
}
