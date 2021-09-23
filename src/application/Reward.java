package application;

import application.helpers.*;
import java.text.DecimalFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Reward extends MainContext {

	@FXML
	private Label rewardLabel;

	public void setScore(double score) {
		// strip out any trailing zeros, e.g. `1.0` -> `1`
		String formattedScore = new DecimalFormat("0.#").format(score);

		//two different messages
		if (score > 3) {
			rewardLabel.setText(
				"Ka pai! You have scored " + formattedScore + " out of 5"
			);
		} else if (score <= 3) {
			rewardLabel.setText(
				"You scored " +
				formattedScore +
				" out of 5, you can certainly do better"
			);
		}
	}

	/** Switches back to home screen on button press */
	public void goHome(ActionEvent e) {
		this.navigateTo("Home.fxml", e);
	}
}
