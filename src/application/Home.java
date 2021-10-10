package application;

import application.helpers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Home extends UIController {

	/** these are the names and values for the TTS Speed ComboBox */
	private static final ObservableList<String> ttsOptionNames = FXCollections.observableArrayList(
		"Hohoro / Fast",
		"Māori / Normal",
		"Pōturi / Slow",
		"Puku Pōturi / Very Slow"
	);
	private static final double[] ttsOptionValues = new double[] {
		0.5,
		1.0,
		1.5,
		2.0,
	};

	@FXML
	private Label errorMsg;

	@FXML
	private Button quitButton;

	@FXML
	private ComboBox<String> ttsSpeedDropdown;

	@Override
	public void onReady() {
		try {
			// add the options to the TTSSpeed ComboBox
			ttsSpeedDropdown.setItems(ttsOptionNames);

			// set the initial value of the ComboBox to be the current TTSSpeed
			for (int i = 0; i < ttsOptionValues.length; i++) {
				if (ttsOptionValues[i] == this.context.getTTSSpeed()) {
					ttsSpeedDropdown.setValue(ttsOptionNames.get(i));
				}
			}

			// register the onChange callback
			ttsSpeedDropdown.setOnAction(event -> {
				double selectedSpeed =
					ttsOptionValues[ttsSpeedDropdown
							.getSelectionModel()
							.getSelectedIndex()];
				this.context.setTTSSpeed(selectedSpeed);
			});
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	/** called when you click the 'Start Game' button */
	public void startGameModule(ActionEvent event) {
		TopicSelection newPage = (TopicSelection) this.navigateTo(
				"TopicSelection.fxml",
				event
			);
		newPage.mode = Game.Mode.GAME;
	}

	/** called when you click the 'Start Practice Quiz' button */
	public void startPracticeModule(ActionEvent event) {
		TopicSelection newPage = (TopicSelection) this.navigateTo(
				"TopicSelection.fxml",
				event
			);
		newPage.mode = Game.Mode.PRACTICE;
	}

	/** called by the 'quit' button. This exits the app */
	public void exit(ActionEvent event) {
		Stage stage = (Stage) quitButton.getScene().getWindow();
		stage.close();
	}

	/** called by the 'Play Sample' button next to the TTS Speed selection */
	public void playSample() {
		Festival.speak(
			"Kia Ora",
			Festival.Language.TE_REO,
			this.context.getTTSSpeed()
		);
	}
}
