package application;

import application.helpers.*;
import application.models.*;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
	private Button playAgainBtn, goHomeBtn;

	@FXML
	private TableView<AnswerTableModel> tableView;

	@FXML
	private SVGPath cloudSVG, goldMedal, silverMedal, bronzeMedal;

	@FXML
	private AnchorPane rewardPane;

	@FXML
	private ImageView fireworks;

	/** whether the game was a practice or real quiz */
	private Game.Mode gameMode;

	private void populateTable(Game.AnswerType[] answers, Topics.Word[] words) {
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<AnswerTableModel, String> col1 = new TableColumn<>("Te Reo");
		col1.setCellValueFactory(new PropertyValueFactory<>("teReo"));
		tableView.getColumns().add(col1);

		TableColumn<AnswerTableModel, String> col2 = new TableColumn<>("English");
		col2.setCellValueFactory(new PropertyValueFactory<>("english"));
		tableView.getColumns().add(col2);

		TableColumn<AnswerTableModel, String> col3 = new TableColumn<>(
			text("outcome")
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
						text("answerType_" + answers[i].name())
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

		goHomeBtn.setText(text("goHome"));
		playAgainBtn.setText(
			gameMode == Game.Mode.GAME
				? text("playAgainGame")
				: text("playAgainPractice")
		);

		String formattedScore = Format.formatScore(score);
		int totalPossiblePoints = gameMode == Game.Mode.GAME ? 10 : 5;
		this.gameMode = gameMode;

		//making all medals invisible
		goldMedal.setVisible(false);
		silverMedal.setVisible(false);
		bronzeMedal.setVisible(false);

		messageLabel.setText(text("clickAnyCloud"));

		//Does not display high score or medals in practice
		if (gameMode == Game.Mode.GAME) {
			highScoreLabel.setVisible(true);
			highScoreLabel.setText(text("highScore", this.context.getHighScore()));
			if (score == 10) {
				rewardLabel.setText(
					text("scoreMsgGold", formattedScore, totalPossiblePoints)
				);

				goldMedal.setVisible(true);
				triggerFireworks();
			} else if ((score < 10) && (score >= 6)) {
				rewardLabel.setText(
					text("scoreMsgSilver", formattedScore, totalPossiblePoints)
				);

				silverMedal.setVisible(true);
				triggerFireworks();
			} else {
				rewardLabel.setText(
					text("scoreMsgBronze", formattedScore, totalPossiblePoints)
				);

				bronzeMedal.setVisible(true);
			}
		} else {
			rewardLabel.setText(
				score > 3
					? text("scoreMsgGold", formattedScore, totalPossiblePoints)
					: text("scoreMsgBronze", formattedScore, totalPossiblePoints)
			);
		}

		populateTable(answers, words);

		// the table is initially hidden until you click a cloud
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
		playAgainBtn.setDisable(true);
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
		goHomeBtn.setDisable(true);
		FX
			.fadeOut(rewardPane)
			.setOnFinished(e -> {
				this.navigateTo("Home.fxml", event);
			});
	}

	/** this runs in an async thread so it's not blocking */
	private void triggerFireworks() {
		new Thread(() -> {
			try {
				URL path = getClass()
					.getClassLoader()
					.getResource("images/fireworks.gif");
				fireworks.setImage(new Image(path.toString(), true));

				fireworks.setFitWidth(1280);
				fireworks.setFitHeight(720);
				fireworks.setX(0);
				fireworks.setY(0);
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
