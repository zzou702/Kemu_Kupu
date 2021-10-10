package application;

import application.helpers.*;
import application.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Reward extends UIController {

	@FXML
	private Label rewardLabel;

	@FXML
	private TableView<AnswerTableModel> tableView;

	private String readableAnswerType(Game.AnswerType answerType) {
		switch (answerType) {
			case CORRECT:
				return "✓ Correct";
			case FAULTED:
				return "✓ Faulted";
			case INCORRECT:
				return "✖ Incorrect";
			case SKIPPED:
				return "✖ Skipped";
			case TOO_SLOW:
				return "✖ Too Slow";
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
			"Your Answer"
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
		Topics.Word[] words
	) {
		String formattedScore = Format.formatScore(score);

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

		populateTable(answers, words);
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
