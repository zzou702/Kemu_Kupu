package application;

import application.helpers.*;
import application.wrappers.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Home extends UIController {

	@FXML
	private Button quitButton, sampleButton;

	@FXML
	private ComboBox<String> ttsSpeedDropdown;

	@FXML
	private AnchorPane homePane;

	@Override
	public void onReady() {
		try {
			FX.fadeIn(homePane);

			// initialize the TTS speed dropdown
			new Dropdown<Double>(
				/* element */ttsSpeedDropdown,
				/* values */FXCollections.observableArrayList(
					"Hohoro \nFast",
					"Māori \nNormal",
					"Pōturi \nSlow",
					"Puku Pōturi \nVery Slow"
				),
				/* keys */new Double[] { 0.5, 1.0, 1.5, 2.0 },
				/* initial value */context.getTTSSpeed()
			) {
				public void onChange(Double newValue) {
					context.setTTSSpeed(newValue);
				}
			};
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	/** called when you click the 'Start Game' button */
	public void startGameModule(ActionEvent event) {
		Festival.emptyQueue();
		FX
			.fadeOut(homePane)
			.setOnFinished(e -> {
				TopicSelection newPage = (TopicSelection) this.navigateTo(
						"TopicSelection.fxml",
						event
					);
				newPage.mode = Game.Mode.GAME;
			});
	}

	/** called when you click the 'Start Practice Quiz' button */
	public void startPracticeModule(ActionEvent event) {
		Festival.emptyQueue();
		FX
			.fadeOut(homePane)
			.setOnFinished(e -> {
				TopicSelection newPage = (TopicSelection) this.navigateTo(
						"TopicSelection.fxml",
						event
					);
				newPage.mode = Game.Mode.PRACTICE;
			});
	}

	/** called by the 'quit' button. This exits the app */
	public void exit(ActionEvent event) {
		Stage stage = (Stage) quitButton.getScene().getWindow();
		stage.close();
	}

	/** called by the 'Play Sample' button next to the TTS Speed selection */
	public void playSample() {
		// Disables the button in a separate thread while festival is speaking
		sampleButton.setDisable(true);

		Festival.speak(
			"Kia Ora",
			Festival.Language.TE_REO,
			this.context.getTTSSpeed(),
			() -> sampleButton.setDisable(false)
		);
	}
}
