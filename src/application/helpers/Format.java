package application.helpers;

import java.text.DecimalFormat;

public class Format {

	/** formats a score to remove any trailing zeros (e.g. 5.00 -> 5) */
	public static String formatScore(double score) {
		return new DecimalFormat("0.##").format(score);
	}

	/** formats the number of seconds as a time, e.g. 70 -> "01:10" */
	public static String formatAsTime(int seconds) {
		return String.format("%02d:%02d", (seconds % 3600) / 60, seconds % 60);
	}
}