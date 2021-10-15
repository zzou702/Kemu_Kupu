package application.helpers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Every Controller extends this class. This means you can access global variables
 * like the TTS speed.
 *
 * It also exposes a navigateTo method which makes it easier to change to a new page.
 */
public abstract class UIController {

	/** the speed that festival will read the word at */
	public AppContext context;

	/**
	 * no-op by default, but can be overridden for classes that need a callback
	 * like initialize() that runs after the main context has been setup, and FXML
	 * is ready.
	 */
	public void onReady() {}

	// we provide two overloads for convenience. You can either supply an event or a node
	// this is required so that we can get a reference to the current stage.
	public UIController navigateTo(String page, ActionEvent event) {
		return navigateTo(page, (Node) event.getSource());
	}

	/**
	 * this is how you get all text in the UI in the user's chosen language
	 * @param messageId corresponds to an ID in the file `text.csv`
	 * @param args arguments for `MessageFormat.format`
	 */
	protected final String text(String messageID, Object... args) {
		return Translations
			.getInstance()
			.get(messageID, context.getLanguage(), args);
	}

	public UIController navigateTo(String page, Node node) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
			Parent newRoot = loader.load();

			// creates new instance of the controller for the corresponding scene
			UIController newController = loader.getController();

			// copy the instance of the AppContext to the new controller
			newController.context = this.context;

			// initialize the scene
			Stage stage = (Stage) node.getScene().getWindow();
			Scene newScene = new Scene(newRoot);
			stage.setScene(newScene);
			stage.show();
			newController.onReady();

			return newController;
		} catch (Exception error) {
			error.printStackTrace();
			return null;
		}
	}
}
