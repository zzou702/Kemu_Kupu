package application;

import application.helpers.*;
import java.text.DecimalFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Reward extends UIController {

	@FXML
	private Label rewardLabel;

	/** called by the Game UI when it completes, to pass us these variables */
	public void initialize(
		double score,
		Game.AnswerType[] answers,
		Topics.Word[] words
	) {
		// strip out any trailing zeros, e.g. `1.0` -> `1`
		String formattedScore = new DecimalFormat("0.#").format(score);

		// two different messages
		if (score > 7) {
			rewardLabel.setText(
				"Ka pai! You have scored " + formattedScore + " out of 10"
			);
		} else {
			rewardLabel.setText(
				"You scored " +
				formattedScore +
				" out of 10, you'll do better next time!"
			);
		}
		// TODO: render a table of `answers` and `words`
	}

	/** Switches back to topic selection screen on button press */
	public void newGame(ActionEvent event) {
		this.navigateTo("TopicSelection.fxml", event);
	}

	/** Switches back to home screen on button press */
	public void goHome(ActionEvent event) {
		this.navigateTo("Home.fxml", event);
	}
}
