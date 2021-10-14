package application;

import application.helpers.*;
import application.models.*;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

public class Reward extends UIController {

	// milliseconds
	private static final int FIREWORKS_HOLD_DURATION = 2500;
	private static final int FIREWORKS_FADE_DURATION = 1000;

	@FXML
	private Label rewardLabel, highScoreLabel, messageLabel;

	@FXML
	private TableView<AnswerTableModel> tableView;

	@FXML
	private SVGPath cloudSVG;

	@FXML
	private AnchorPane rewardPane;

	@FXML
	private ImageView fireworks;

	/** whether the game was a practice or real quiz */
	private Game.Mode gameMode;

	private String readableAnswerType(Game.AnswerType answerType) {
		switch (answerType) {
			case CORRECT:
				return "✓ Tika / Correct";
			case FAULTED:
				return "✓ Hāwhe kaute / Faulted";
			case INCORRECT:
				return "✖ Hē / Incorrect";
			case SKIPPED:
				return "✖ Pahemo / Skipped";
			case TOO_SLOW:
				return "✖ Tō pōturi hoki / Too Slow";
			default:
				return ""; // will never happen
		}
	}

	private void populateTable(Game.AnswerType[] answers, Topics.Word[] words) {
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<AnswerTableModel, String> col1 = new TableColumn<>("Te Reo");
		col1.setCellValueFactory(new PropertyValueFactory<>("teReo"));
		tableView.getColumns().add(col1);

		TableColumn<AnswerTableModel, String> col2 = new TableColumn<>("English");
		col2.setCellValueFactory(new PropertyValueFactory<>("english"));
		tableView.getColumns().add(col2);

		TableColumn<AnswerTableModel, String> col3 = new TableColumn<>(
			"Putanga / Outcome"
		);
		col3.setCellValueFactory(new PropertyValueFactory<>("status"));
		tableView.getColumns().add(col3);

		for (int i = 0; i < answers.length; i++) {
			tableView
				.getItems()
				.add(
					new AnswerTableModel(
						words[i].teReo,
						words[i].english,
						readableAnswerType(answers[i])
					)
				);
		}
	}

	/** called by the Game UI when it completes, to pass us these variables */
	public void initialize(
		double score,
		Game.AnswerType[] answers,
		Topics.Word[] words,
		Game.Mode gameMode
	) {
		FX.fadeIn(rewardPane);
		this.triggerFireworks(); // runs in an async thread so we call this first

		String formattedScore = Format.formatScore(score);
		int totalPossiblePoints = gameMode == Game.Mode.GAME ? 10 : 5;
		this.gameMode = gameMode;

		// two different messages
		if (score > 7) {
			rewardLabel.setText(
				"Ka pai! You have scored " +
				formattedScore +
				" out of " +
				totalPossiblePoints
			);
		} else {
			rewardLabel.setText(
				"Auare ake! You scored " +
				formattedScore +
				" out of " +
				totalPossiblePoints +
				", \n you'll do better next time!"
			);
		}

		messageLabel.setText("Click any cloud to view/close your statistics");

		//Does not display high score in practice
		if (gameMode == Game.Mode.GAME) {
			highScoreLabel.setVisible(true);
			highScoreLabel.setText("High Score: " + this.context.getHighScore());
		}

		populateTable(answers, words);

		tableView.setVisible(false);
		cloudSVG.setVisible(false);
	}

	/** Toggles the statistics of the game */
	public void toggleStats(ActionEvent event) {
		if (tableView.isVisible()) {
			tableView.setVisible(false);
			cloudSVG.setVisible(false);
		} else {
			tableView.setVisible(true);
			cloudSVG.setVisible(true);
		}
	}

	/** Switches back to topic selection screen on button press */
	public void newGame(ActionEvent event) {
		FX
			.fadeOut(rewardPane)
			.setOnFinished(e -> {
				TopicSelection newPage = (TopicSelection) this.navigateTo(
						"TopicSelection.fxml",
						event
					);
				newPage.mode = gameMode; // start the new game in the same mode as the current game
			});
	}

	/** Switches back to home screen on button press */
	public void goHome(ActionEvent event) {
		FX
			.fadeOut(rewardPane)
			.setOnFinished(e -> {
				this.navigateTo("Home.fxml", event);
			});
	}

	private void triggerFireworks() {
		new Thread(() -> {
			try {
				URL path = getClass()
					.getClassLoader()
					.getResource("images/fireworks.gif");
				fireworks.setImage(new Image(path.toString(), true));
				fireworks.setVisible(true);

				// wait for the gif to finish, then hide it
				// this is acceptable since it's in an async thread
				Thread.sleep(FIREWORKS_HOLD_DURATION);

				FadeTransition fireworksFade = FX.fadeOut(
					fireworks,
					FIREWORKS_FADE_DURATION
				);
				fireworksFade.setOnFinished(e -> fireworks.setVisible(false));
			} catch (Exception error) {
				error.printStackTrace();
			}
		})
			.start();
	}
}
