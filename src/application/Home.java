package application;

import application.helpers.*;
import application.wrappers.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Home extends UIController {

	@FXML
	private Label ttsLabel;

	@FXML
	private Button quitButton, sampleButton, newGameBtn, newPracticeBtn;

	@FXML
	private ComboBox<String> ttsSpeedDropdown, languageDropdown;

	@FXML
	private AnchorPane homePane;

	@Override
	public void onReady() {
		try {
			updateTranslations();
			FX.fadeIn(homePane);
			initHomeTTSDropDown();
			initLangDropdown();
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	private void initHomeTTSDropDown() {
		new Dropdown<Double>(
			/* element */ttsSpeedDropdown,
			/* values */FXCollections.observableArrayList(
				text("ttsSpeed_fast"),
				text("ttsSpeed_normal"),
				text("ttsSpeed_slow"),
				text("ttsSpeed_verySlow")
			),
			/* keys */new Double[] { 0.5, 1.0, 1.5, 2.0 },
			/* initial value */context.getTTSSpeed()
		) {
			public void onChange(Double newValue) {
				context.setTTSSpeed(newValue);
			}
		};
	}

	private void initLangDropdown() {
		new Dropdown<String>(
			/* element */languageDropdown,
			/* values */FXCollections.observableArrayList(
				"English",
				"te reo Māori",
				"Ngā Reo e Rua (both)"
			),
			/* keys */new String[] { "en", "mi", "both" },
			/* initial value */context.getLanguage()
		) {
			public void onChange(String newValue) {
				context.setLanguage(newValue);
				updateTranslations();
				initHomeTTSDropDown();
			}
		};
	}

	/** called when a new value is selected in the language dropdown */
	private void updateTranslations() {
		ttsLabel.setText(text("ttsLabel"));
		sampleButton.setText(text("playSample"));
		quitButton.setText(text("quit"));
		newGameBtn.setText(text("newGame"));
		newPracticeBtn.setText(text("newPracticeGame"));
	}

	/** called when you click the 'Start Game' button */
	public void startGameModule(ActionEvent event) {
		newGameBtn.setDisable(true);
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
		newPracticeBtn.setDisable(true);
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
