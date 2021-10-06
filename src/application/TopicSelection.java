package application;

import application.helpers.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class TopicSelection extends UIController {

	/** the list of possible topics, populated by the initialize() method */
	private ArrayList<Topics.Topic> topicsList;

	@FXML
	private ListView<String> topicListView;

	@FXML
	private Button startButton;

	@FXML
	private void initialize() {
		try {
			startButton.setDisable(true); // because no topic selected

			topicsList = Topics.getTopics();

			// JavaFX wants a list of Strings, not a list of Topics
			ArrayList<String> topicTitleList = new ArrayList<>();
			for (Topics.Topic topic : topicsList) {
				topicTitleList.add(topic.title);
			}
			topicListView.getItems().addAll(topicTitleList);

			// as soon as any topic is selected, enable the startButton
			topicListView
				.getSelectionModel()
				.selectedItemProperty()
				.addListener((observable, oldValue, newValue) ->
					startButton.setDisable(false)
				);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	/** called when you click the start button */
	public void startGame(ActionEvent event) {
		try {
			// because the start button is disabled if nothing is selected, we know this is
			// always a valid index
			int chosenTopicIndex = topicListView
				.getSelectionModel()
				.getSelectedIndex();
			Topics.Topic chosenTopic = topicsList.get(chosenTopicIndex);

			// navigate to the next page, then call the new page's startGame method
			// to pass the chosenTopic to it.
			Game newController = (Game) this.navigateTo("Game.fxml", event);
			newController.startGame(chosenTopic);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	/** called when you click the back button */
	public void goBack(ActionEvent event) {
		this.navigateTo("Home.fxml", event);
	}
}
