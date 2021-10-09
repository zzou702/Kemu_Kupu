package application.helpers;

import javafx.scene.control.Alert;

/**
 * a class which shows a popup with help/instructions.
 * Usage: `Help.showPopup(Help.Category.GAME)`
 */
public class Help {

	public static enum Category {
		TOPIC_SLECTION,
		GAME,
	}

	private static String getHelpMessage(Category category) {
		switch (category) {
			case TOPIC_SLECTION:
				return "Click a topic. Then click Start to begin.";
			case GAME:
				return (
					"Type the word into the textbox to play. " +
					"\nClick enter or the submit button to test the word. " +
					"\nThe repeat button reads out the word again. " +
					"\nThe skip button moves onto the next word" +
					"\nClick the macron buttons to add vowels with macrons"
				);
			default:
				return ""; // will never happen
		}
	}

	public static void showPopup(Category category) {
		Alert instructions = new Alert(Alert.AlertType.INFORMATION);
		instructions.setTitle("Āwhina / Instructions");
		instructions.setHeaderText(null);
		instructions.setContentText(getHelpMessage(category));
		instructions.show();
	}
}
