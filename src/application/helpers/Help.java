package application.helpers;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

/**
 * a class which shows a popup with help/instructions.
 * Usage: `Help.showPopup(Help.Category.GAME)`
 */
public class Help {

	public static enum Category {
		TOPIC_SLECTION,
		GAME,
	}

	/**
	 * gets the appropriate help message for the current page/category.
	 *
	 * We always show the hint in both languages, in case the user
	 * is stuck or confused.
	 *
	 * The comments are the transliteral translation
	 */
	private static String getHelpMessage(Category category) {
		switch (category) {
			case TOPIC_SLECTION:
				return (
					"Ka taea e koe te kōwhiri marau, muri iho pāwhiri te pātene tīmata." + // You can select a topic, afterwards press the start button.
					"\n" +
					"\nClick a topic. Then click Start to begin."
				);
			case GAME:
				return (
					"Patohia te kupu ki te pouaka kupu, ati pāwhiri te pātuhi tāuru." + // Type the word into the text box, then press the enter key.
					"\nKi te whakarongo ano i te kupu korero, pāwhiri te 'toai' pātene." + // To hear the spoken word again, click the 'repeat' button.
					"\nKi te kore koe e mohio, pāwhiri 'pahemo'." + // If you don't know, press 'skip'
					"\nPāwhiri nga pātene tohutō, ki te kōkuhu i tohutō" + // Press the button [of the] macrons(pl), to insert macrons(pl).
					"\n" +
					"\nType the word into the textbox to play. " +
					"\nClick enter or the submit button to test the word. " +
					"\nThe repeat button reads out the word again. " +
					"\nThe skip button moves onto the next word." +
					"\nClick the macron buttons to add vowels with macrons."
				);
			default:
				return ""; // will never happen
		}
	}

	public static void showPopup(Category category) {
		Alert instructions = new Alert(Alert.AlertType.INFORMATION);
		instructions.setTitle("Āwhina / Help");
		instructions.setHeaderText(null);
		instructions.setContentText(getHelpMessage(category));
		instructions.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		instructions.show();
	}
}
