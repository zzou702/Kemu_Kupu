package application;

import application.helpers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Reward extends MainContext {

	@FXML
	private Label rewardLabel;

	public void setScore(int score) {
		//two different messages
		if (score > 3) {
			rewardLabel.setText(
				"Congragulations! You have scored " + score + " out of 5"
			);
		} else if (score <= 3) {
			rewardLabel.setText(
				"You have scored " + score + " out of 5, you can definitely do better"
			);
		}
	}

	/** Switches back to home screen on button press */
	public void goHome(ActionEvent e) {
		this.navigateTo("Home.fxml", e);
	}
}
