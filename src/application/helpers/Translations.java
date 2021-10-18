package application.helpers;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This singleton class reads the translated messages from the csv file.
 * You can then fetch a message in any language.
 * Don't use this class directly, use `this.text("messageId")`
 * from any controller.
 */
public class Translations {

	private static final String TRANSLATION_FILE = "./src/text.csv";

	// this is a singleton class
	private static Translations instance;

	public static Translations getInstance() {
		if (instance == null) instance = new Translations();
		return instance;
	}

	// a map keyed by the messageId from the CSV file. The value is the entire row
	private HashMap<String, String[]> translationMap = new HashMap<>();

	public Translations() {
		try {
			Scanner scanner = new Scanner(new File(TRANSLATION_FILE));

			scanner.nextLine(); // throw away line 1, it's the comment
			scanner.nextLine(); // throw away line 2, it's blank
			scanner.nextLine(); // throw away line 3, it's the headers

			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(",");

				// skip invalid lines
				if (line.length != 4) continue;

				translationMap.put(line[0], line);
			}
			scanner.close();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	/**
	 * this is the main method to get a message in the user's chosen language
	 * @param messageId corresponds to an ID in the file `text.csv`
	 * @param args arguments for `MessageFormat.format`
	 */
	public String get(String messageId, String languageId, Object... args) {
		int langIndex = languageId.equals("en")
			? 1
			: languageId.equals("mi") ? 2 : 3;

		String[] items = translationMap.get(messageId);

		// this will never happen to users, only when developing the app
		if (items == null) return "MISSING TRANSLATION FOR " + messageId;

		return MessageFormat.format(
			// commas would break the format, so you can use a semicolon to insert a literal comma
			// a literal "\n" also gets converted to a line break
			items[langIndex].replaceAll(";", ",").replaceAll("\\\\n", "\n"),
			args
		);
	}
}
