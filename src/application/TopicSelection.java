package application;

import application.helpers.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class TopicSelection extends MainContext {

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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** called when you click the start button */
	public void startGame(ActionEvent e) {
		try {
			// because the start button is disabled if nothing is selected, we know this is
			// always a valid index
			int chosenTopicIndex = topicListView
				.getSelectionModel()
				.getSelectedIndex();
			Topics.Topic chosenTopic = topicsList.get(chosenTopicIndex);

			Game newController = (Game) this.navigateTo("Game.fxml", e);
			newController.startGame(chosenTopic);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/** called when you click the back button */
	public void goBack(ActionEvent e) {
		this.navigateTo("Home.fxml", e);
	}
}
