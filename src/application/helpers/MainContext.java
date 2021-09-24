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
public abstract class MainContext {

	/** the speed that festival will read the word at */
	// NOTE: this is not used for A3, but will be used in the project
	public int TTSSpeed;

	public MainContext navigateTo(String page, ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
			Parent newRoot = loader.load();

			// creates new instance of the controller for the corresponding scene
			MainContext newController = loader.getController();

			// copy global context values to the new controller
			newController.TTSSpeed = this.TTSSpeed;

			// initialize the scene
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene newScene = new Scene(newRoot);
			stage.setScene(newScene);
			stage.show();

			return newController;
		} catch (Exception error) {
			error.printStackTrace();
			return null;
		}
	}
}
