package application;

import application.helpers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Home extends MainContext {

	@FXML
	private Label errorMsg;

	@FXML
	private Button quitButton;

	/** called when you click the 'Start Game' button */
	public void goToTopicSelection(ActionEvent event) {
		this.navigateTo("TopicSelection.fxml", event);
	}

	/** called by the 'quit' button. This exits the app  */
	public void exit(ActionEvent event) {
		Stage stage = (Stage) quitButton.getScene().getWindow();
		stage.close();
	}
}
