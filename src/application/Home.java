package application;

import application.helpers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Home extends MainContext {

	@FXML //Declaration of widgets made in SceneBuilder
	private Label errorMsg;

	@FXML
	private Button quitButton;

	/** called when you click the 'Start Game' button */
	public void goToTopicSelection(ActionEvent e) {
		this.navigateTo("TopicSelection.fxml", e);
	}

	public void exit(ActionEvent e) { //Quits the game
		Stage stage = (Stage) quitButton.getScene().getWindow();
		stage.close();
	}
}
