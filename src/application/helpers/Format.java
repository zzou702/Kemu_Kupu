package application.helpers;

import java.text.DecimalFormat;

public class Format {

	/** formats a score to remove any trailing zeros (e.g. 5.00 -> 5) */
	public static String formatScore(double score) {
		// based on https://stackoverflow.com/a/14205029/5470183
		return new DecimalFormat("0.##").format(score);
	}

	/** formats the number of seconds as a time, e.g. 70 -> "01:10" */
	public static String formatAsTime(int seconds) {
		// based on https://stackoverflow.com/a/22545885/5470183
		return String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);
	}

	/**
	 * converts a word into underscores for the hint, e.g. "Hello" -> "_ _ _ _ _"
	 * If `includeSomeLetters` is true, then the result would be "H _ _ _ o"
	 */
	public static String getUnderscoreHint(
		String word,
		Boolean includeSomeLetters,
		Boolean includeAllLetters
	) {
		int lastIndex = word.length() - 1;

		StringBuilder withUnderscore = new StringBuilder(
			word
				.replaceAll("[\\wāēīōūĀĒĪŌŪ]", "_") // replace (A-Z, a-z, and macrons) with underscores
				.replaceAll(".", "$0 ") // then add a space between every character
				.trim() // trim to remove the last space character
		);

		if (includeSomeLetters) {
			// add back the first and last letter.
			withUnderscore.setCharAt(0, word.charAt(0));
			withUnderscore.setCharAt(lastIndex * 2, word.charAt(lastIndex));
			// we use `lastIndex * 2` because withUnderscore is double as long as
			// the original, since it has spaces between every letter.
		}

		if (includeAllLetters) {
			// add back all letters
			for (int i = 0; i < withUnderscore.length(); i += 2) {
				withUnderscore.setCharAt(i, word.charAt(i / 2));
			}
		}

		return withUnderscore.toString();
	}
}
