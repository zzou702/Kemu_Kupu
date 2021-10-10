package application;

import application.helpers.*;

import java.io.IOException;
import java.text.DecimalFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Reward extends UIController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private Game.AnswerType[] correctness;
	private Topics.Word[] words;
	
	@FXML
	private Label rewardLabel;
	@FXML
	private ListView<String> statsList;
	@FXML
	private SVGPath cloudShape;
	

	/** called by the Game UI when it completes, to pass us these variables */
	public void initialize(
		double score,
		Game.AnswerType[] answers,
		Topics.Word[] words
	) {
		// strip out any trailing zeros, e.g. `1.0` -> `1`
		String formattedScore = new DecimalFormat("0.#").format(score);

		// two different messages
		if (score > 3) {
			rewardLabel.setText(
				"Ka pai! You have scored " + formattedScore + " out of 5"
			);
		} else {
			rewardLabel.setText(
				"You scored " +
				formattedScore +
				" out of 5, you can certainly do better"
			);
		}
		
		//making the listview and the shape surrounding it invisible
		statsList.setVisible(false);
		cloudShape.setVisible(false);
		
		correctness = answers;
		this.words = words;
		// TODO: render a table of `answers` and `words`
		System.out.println(answers[1]);
		System.out.println(words[1].teReo);
	}
	
	public void showCorrectStats(ActionEvent event){
		
		//toggle the listview for the statistics
		if (statsList.isVisible()) {
			statsList.setVisible(false);
			cloudShape.setVisible(false);
			
			/*clear the stats and quitt eh function when the listview 
			is closed */
			statsList.getItems().clear();
			return;
		}else {
			statsList.setVisible(true);
			cloudShape.setVisible(true);
		}

		//testing
		statsList.getItems().add("hi");
		
		//TODO: adding the correct words to the listview
		/*
		for (int i = 0; i < words.length; i++) {
			if (correctness[i].equals("CORRECT")) {
				statsList.getItems().add("Te Reo: " + words[i].teReo 
						+ "\nEnglish: " + words[i].english);
			}
		}*/

	}
	
	public void showFaultyStats(ActionEvent event) {
		//toggle the listview for the statistics
				if (statsList.isVisible()) {
					statsList.setVisible(false);
					cloudShape.setVisible(false);
					
					/*clear the stats and quitt eh function when the listview 
					is closed */
					statsList.getItems().clear();
					return;
				}else {
					statsList.setVisible(true);
					cloudShape.setVisible(true);
				}

				//testing
				statsList.getItems().add("hi");
				
				//TODO: adding the faulty words to the listview
	}
	
	public void showIncorrectStats(ActionEvent event) {
		//toggle the listview for the statistics
				if (statsList.isVisible()) {
					statsList.setVisible(false);
					cloudShape.setVisible(false);
					
					/*clear the stats and quitt eh function when the listview 
					is closed */
					statsList.getItems().clear();
					return;
				}else {
					statsList.setVisible(true);
					cloudShape.setVisible(true);
				}

				//testing
				statsList.getItems().add("hi");
				
				//TODO: adding the incorrect words to the listview
				
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
