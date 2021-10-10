package application.helpers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Unit tests for the Format class */
class FormatTest {

	@Test
	void testFormatScore() {
		assertEquals(Format.formatScore(5.00), "5");
		assertEquals(Format.formatScore(5.10), "5.1");
		assertEquals(Format.formatScore(5.11), "5.11");
		assertEquals(Format.formatScore(5), "5");
	}

	@Test
	void testFormatAsTime() {
		assertEquals(Format.formatAsTime(0), "00:00");
		assertEquals(Format.formatAsTime(1), "00:01");
		assertEquals(Format.formatAsTime(59), "00:59");
		assertEquals(Format.formatAsTime(60), "01:00");
		assertEquals(Format.formatAsTime(61), "01:01");
		assertEquals(Format.formatAsTime(3599), "59:59");

		// this is expected, we will never get to 1 hour so it's not an issue
		assertEquals(Format.formatAsTime(3600), "00:00");
		assertEquals(Format.formatAsTime(3601), "00:01");
	}
}
