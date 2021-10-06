package application.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Scanner;

/**
 * This is a singleton, only one instance of this class is ever created.
 * This instance of AppContext can be accessed from any page using `this.context`.
 * It stores the state that affects the whole app, like the TTS Speed and the HighScore.
 */
public class AppContext {

	private static final String PREF_FILE = "./.preferences";

	/** 1 is the normal speed. >1 is faster, <1 is slower */
	private double TTSSpeed = 1.0;
	/** the highest score the user has ever achieved */
	private double highScore = 0.0;

	public AppContext() {
		// read settings from disk to initially populate the context
		try {
			Scanner scanner = new Scanner(new File(PREF_FILE));

			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("\\|");

				// skip invalid lines
				if (line.length != 2) continue;

				String key = line[0];
				String value = line[1];

				// skip badly formatted lines
				if (key == null || key == "" || value == null || value == "") continue;

				switch (key) {
					case "TTSSpeed":
						TTSSpeed = Double.parseDouble(value);
						break;
					case "highScore":
						highScore = Double.parseDouble(value);
						break;
					// skip unknown items in the file
				}
			}
			scanner.close();
		} catch (Exception error) {
			// if we can't read the preferences, then the default values will be used
			error.printStackTrace(); // TODO: temp
		}
	}

	private void updatePreferencesFile() {
		try {
			// TODO: make this non-blocking?

			String newFile = MessageFormat.format(
				"TTSSpeed|{0}\nhighScore|{1}\n",
				/* 0 */TTSSpeed,
				/* 1 */highScore
			);

			BufferedWriter writer = new BufferedWriter(new FileWriter(PREF_FILE));
			writer.write(newFile);
			writer.close();
		} catch (IOException error) {
			// if we can't save the preferences do nothing,
			// next time they open the game the default values
			// will be used
		}
	}

	// getters and setters. Setters must save the new preferences to disk

	public double getTTSSpeed() {
		return TTSSpeed;
	}

	public void setTTSSpeed(double newSpeed) {
		TTSSpeed = newSpeed;
		updatePreferencesFile();
	}

	public double getHighScore() {
		return highScore;
	}

	public void setHighScore(double newHighScore) {
		highScore = newHighScore;
		updatePreferencesFile();
	}
}
