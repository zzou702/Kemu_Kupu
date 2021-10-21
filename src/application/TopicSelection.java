package application;

import application.helpers.*;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class TopicSelection extends UIController {

	/** the current mode (practice or game) */
	public Game.Mode mode;

	/** the list of possible topics, populated by the initialize() method */
	private ArrayList<Topics.Topic> topicsList;

	@FXML
	private ListView<String> topicListView;

	@FXML
	private Button startButton, backButton, helpButton;

	@FXML
	private Label topicSelectionHeader;

	@FXML
	private ImageView topicDisplay;

	@FXML
	private Rectangle imageBackground;

	@FXML
	private AnchorPane topicPane;

	@Override
	public void onReady() {
		try {
			startButton.setText(text("start"));
			backButton.setText(text("back"));
			helpButton.setText(text("help"));
			topicSelectionHeader.setText(text("topicSelectionHeader"));

			FX.fadeIn(topicPane);

			startButton.setDisable(true); // because no topic selected

			topicsList = Topics.getTopics(context.getLanguage());

			// JavaFX wants a list of Strings, not a list of Topics
			ArrayList<String> topicTitleList = new ArrayList<>();
			for (Topics.Topic topic : topicsList) {
				topicTitleList.add(topic.title);
			}
			topicListView.getItems().addAll(topicTitleList);

			// register the `onSelect` callback
			topicListView
				.getSelectionModel()
				.selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> onSelect());
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	private Topics.Topic getSelection() {
		// because the start button is disabled if nothing is selected, we know this is
		// always a valid index
		int chosenTopicIndex = topicListView.getSelectionModel().getSelectedIndex();
		return topicsList.get(chosenTopicIndex);
	}

	/** called when you click the start button */
	public void startGame(ActionEvent event) {
		startButton.setDisable(true);
		try {
			Topics.Topic chosenTopic = getSelection();

			// navigate to the next page, then call the new page's startGame method
			// to pass the chosenTopic to it.
			Game newController = (Game) this.navigateTo("Game.fxml", event);
			newController.startGame(chosenTopic, mode);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}

	/** Called when user chooses the different topics */
	public void onSelect() {
		// as soon as any topic is selected, enable the startButton
		startButton.setDisable(false);

		String topicName = getSelection().fileName;

		// Changes the image to match the corresponding topic
		URL path = getClass()
			.getClassLoader()
			.getResource(
				// replace .csv with .jpg
				"images/" + topicName.substring(0, topicName.length() - 4) + ".jpg"
			);

		// check that an image exists for this topic. If not, show no image
		if (path == null) {
			topicDisplay.setVisible(false);
			imageBackground.setVisible(false);
		} else {
			topicDisplay.setVisible(true);
			topicDisplay.setImage(new Image(path.toString(), true));
			imageBackground.setVisible(true);
		}
	}

	//Called when help button is pressed
	public void help(ActionEvent event) {
		Help.showPopup(Help.Category.TOPIC_SLECTION);
	}

	/** called when you click the back button */
	public void goBack(ActionEvent event) {
		backButton.setDisable(true);
		FX
			.fadeOut(topicPane)
			.setOnFinished(e -> {
				this.navigateTo("Home.fxml", event);
			});
	}
}
