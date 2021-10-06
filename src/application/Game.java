package application;

import application.helpers.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Game extends UIController {

	/** the random words for this quiz */
	private Topics.Word[] words;

	/** the current question number, starting at 0 */
	private int currentWordIndex = 0;

	/** 1 if this is first attempt at this word. 2 if it is the second attempt */
	private int attemptNumber = 1;

	/** the current score, initially 0 **/
	private double scoreCount = 0;

	@FXML
	private Label statusLabel;

	@FXML
	private TextField answerField;

	@FXML
	private Label countLabel;

	@FXML
	private Label quizTitle;

	@FXML
	private Label scoreLabel;

	/** The following 5 methods insert a vowel with a macron on button press **/
	public void insertA(ActionEvent event) {
		answerField.insertText(answerField.getLength(), "ā");
	}

	public void insertE(ActionEvent event) {
		answerField.insertText(answerField.getLength(), "ē");
	}

	public void insertI(ActionEvent event) {
		answerField.insertText(answerField.getLength(), "ī");
	}

	public void insertO(ActionEvent event) {
		answerField.insertText(answerField.getLength(), "ō");
	}

	public void insertU(ActionEvent event) {
		answerField.insertText(answerField.getLength(), "ū");
	}

	/** called by the topic selection page when it renders this page */
	public void startGame(Topics.Topic topic) throws Exception {
		quizTitle.setText(topic.title);
		words = topic.getRandomWords();

		this.speakCurrentWord();
		this.refreshUI();
	}

	/** this method updates the UI at the start of each question */
	private void refreshUI() {
		countLabel.setText(
			MessageFormat.format(
				"Spell word {0} of {1}\nUiui: {0} ō {1}",
				/* 0 */currentWordIndex + 1,
				/* 1 */words.length
			)
		);
		scoreLabel.setText(
			// strip out any trailing zeros, e.g. `1.0` -> `1`
			"Kaute (Score): " + (new DecimalFormat("0.#").format(scoreCount))
		);
	}

	/** called by the back button */
	public void switchHome(ActionEvent event) {
		this.navigateTo("Home.fxml", event);
	}

	/** called by the repeat button, and also by other methods */
	public void speakCurrentWord() {
		Festival.speak(
			words[currentWordIndex].teReo,
			Festival.Language.TE_REO,
			this.context.getTTSSpeed()
		);
	}

	/** goes to the next word after the user passed, failed, or skipped the previous word */
	public void nextWord(ActionEvent event) {
		currentWordIndex++;
		attemptNumber = 1; // reset the number of attempts, since this is a new word
		answerField.clear();

		// check if we just completed the final word in the quiz
		if (currentWordIndex == words.length) {
			// we are now done
			Reward rewardPage = (Reward) this.navigateTo("Reward.fxml", event);
			rewardPage.setScore(scoreCount);

			// save this score as a high-score if it's the best they've ever achieved
			if (scoreCount > this.context.getHighScore()) {
				this.context.setHighScore(scoreCount);
			}

			return;
		}

		// if we get to here, the game is not over.
		// so move to the next question
		this.speakCurrentWord();
		this.refreshUI();
	}

	/** called by the skip button */
	public void skipWord(ActionEvent event) {
		// Writes encouraging message
		statusLabel.setText("Chin up, you've got the next one!");
		nextWord(event);
	}

	/** called when you click the submit button */
	public void submit(ActionEvent event) {
		statusLabel.setText("");
		String usersAnswer = answerField.getText(); // Gets the value in the text field
		String correctAnswer = words[currentWordIndex].teReo;

		Answer.Correctness correctness = Answer.checkAnswer(
			usersAnswer,
			correctAnswer
		);

		if (correctness == Answer.Correctness.CORRECT) {
			// Answer is completely correct
			statusLabel.setText("Correct!");

			// If answered correctly on second attempt, add half point. Otherwise add full point
			if (attemptNumber == 2) {
				scoreCount += 0.5;
			} else {
				scoreCount++;
			}

			nextWord(event);
		} else {
			// user got the word wrong
			if (attemptNumber == 1) {
				// If user has only gotten it wrong once, read the word again and wait for answer
				attemptNumber = 2;

				String hintPrefix = correctness == Answer.Correctness.ONLY_MACRONS_WRONG
					? "Almost right! Check the macrons. "
					: correctness == Answer.Correctness.ONLY_SYNTAX_WRONG
						? "Almost right! Check your spaces and hyphens. "
						: "Incorrect, try once more. "; // if totally wrong

				statusLabel.setText(
					hintPrefix +
					" Hint: The second letter is '" +
					correctAnswer.charAt(1) +
					"', and the English word is '" +
					words[currentWordIndex].english +
					"'."
				);

				answerField.clear();
			} else {
				// User has gotten it wrong twice. Writes encouraging message
				statusLabel.setText("Incorrect. Chin up, you've got the next one!");
				nextWord(event);
			}
		}
	}
}
