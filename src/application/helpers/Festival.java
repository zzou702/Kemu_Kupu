package application.helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.MessageFormat;

/**
 * A class which calls festival in a non-blocking fashion.
 *
 * @example:
 * Festival.speak("Tāmaki", Festival.Language.TE_REO)
 * Festival.speak("Hello", Festival.Language.ENGLISH)
 */
public class Festival {

	/**
	 * due to a bug with our version of festival, we have to write
	 * the command to disk instead of using festival --pipe.
	 */
	private static final String TEMP_FILE = "./.tmp.scm";

	/**
	 * true if we are currently speaking a word.
	 * We keep track of this so that we never run
	 * two instances of festival in two separate threads,
	 * since that crashes festival.
	 */
	private static boolean busy = false;

	public static enum Language {
		ENGLISH,
		TE_REO,
	}

	public static void speak(String phrase, Language lang) {
		if (busy) return; // do not create a new thread if one is currently active

		busy = true;
		Runnable callback = () -> {
			try {
				String voice = lang.equals(Language.ENGLISH)
					? "voice_akl_nz_jdt_diphone"
					: "voice_akl_mi_pk06_cg";

				// There are bugs with the te reo voice that can crash festival, so we need
				// to be careful about what string we pass to it. Hyphens and capital macrons
				// are problematic (see piazza #159 and #167)
				String safePhrase = phrase.replace("-", " ").toLowerCase();

				// this is unfortunately a two step process, we generate a .scm file with the festival
				// instructions, then write it to disk, and then call festival
				String scmFile = MessageFormat.format(
					"({0})\n(SayText \"{1}\")\n",
					/* 0 */voice,
					/* 1 */safePhrase
				);
				BufferedWriter writer = new BufferedWriter(new FileWriter(TEMP_FILE));
				writer.write(scmFile);
				writer.close();

				Bash.exec("festival -b " + TEMP_FILE);
				busy = false;
			} catch (Exception ex) {
				ex.printStackTrace();
				busy = false;
			}
		};
		Thread thread = new Thread(callback);
		thread.setDaemon(true);
		thread.start();
	}
}
