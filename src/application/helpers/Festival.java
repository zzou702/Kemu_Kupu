package application.helpers;

import java.io.IOException;

/**
 * @example:
 * Festival.speak("Tāmaki", Festival.Language.TE_REO)
 * Festival.speak("Hello", Festival.Language.ENGLISH)
 */
public class Festival {

	public static enum Language {
		ENGLISH,
		TE_REO,
	}

	public static void speak(String phrase, Language lang) {
		Runnable callback = () -> {
			try {
				String voice = lang.equals(Language.ENGLISH)
					? "voice_akl_nz_jdt_diphone"
					: "voice_akl_mi_pk06_cg";

				Bash.exec(
					"echo -e \"({0})\\n(SayText \\\"{1}\\\")\" | festival --pipe",
					/* 0 */voice,
					/* 1 */phrase
				);
			} catch (IOException ex) {
				// do nothing. lambda's can't throw exceptions
				ex.printStackTrace();
			}
		};
		Thread thread = new Thread(callback);
		thread.setDaemon(true);
		thread.start();
	}
}
