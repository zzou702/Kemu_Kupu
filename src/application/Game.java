package application;

import application.helpers.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * This class is used for both the practice module,
 * and the games module. The field `mode` will tell
 * you which module this is.
 *
 * Key differences:
 *  - practice mode does not affect the high-score.
 *  - practice mode gives you a second chance at each word, games mode does not.
 *  - practice mode shows you the correct answer after each question, games mode does't.
 */

public class Game extends UIController {

	/** represnts the type of quiz that's being played (practice or game) */
	public enum Mode {
		PRACTICE,
		GAME,
	}

	/** represnts the type of answer that the user provided for a question */
	public enum AnswerType {
		CORRECT,
		FAULTED, // this means they got it incorrect on the first attempt, then correct. Only possible in practice mode
		INCORRECT,
	}

	/** the current mode (practice or game) */
	private Mode mode;

	/** the random words for this quiz */
	private Topics.Word[] words;

	/** the score that the user got for each word */
	private AnswerType answers[] = new AnswerType[Topics.NUM_WORDS];

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

	@FXML
	private Label lengthLabel;

	/** This method inserts a vowel with a macron on button press. This method is used by 5 buttons **/
	public void insertMacron(ActionEvent event) {
		/** the character with the macron */
		String character = (String) ((Node) event.getSource()).getUserData();
		answerField.insertText(answerField.getLength(), character);

		// move the cursor back to the textField and re-focus on it.
		// the allows the user to resume typing immediately.
		answerField.requestFocus();
		answerField.positionCaret(answerField.getLength());
	}

	/** called by the topic selection page when it renders this page */
	public void startGame(Topics.Topic topic, Mode mode) throws Exception {
		this.mode = mode;
		quizTitle.setText(
			(mode == Mode.PRACTICE ? "Practice: " : "") + topic.title
		);
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

		lengthLabel.setText(
			"Word Length: " + words[currentWordIndex].teReo.length()
		);

		scoreLabel.setText(
			// strip out any trailing zeros, e.g. `1.0` -> `1`
			"Kaute (Score): " + (new DecimalFormat("0.#").format(scoreCount))
		);
	}

	// Called when help button is pressed
	public void help(ActionEvent event) {
		Alert instructions = new Alert(AlertType.INFORMATION);
		instructions.setTitle("Instructions");
		instructions.setHeaderText(null);
		instructions.setContentText(
			"Type the word into the textbox to play. " +
			"\nClick enter or the submit button to test the word. " +
			"\nThe repeat button reads out the word again. " +
			"\nThe skip button moves onto the next word" +
			"\nClick the macron buttons to add vowels with macrons"
		);
		instructions.show();
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
			rewardPage.initialize(scoreCount, answers, words);

			// save this score as a high-score if it's the best they've ever achieved
			// but only if we're in game mode. practice mode does not count.
			if (scoreCount > this.context.getHighScore() && mode == Mode.GAME) {
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
				answers[currentWordIndex] = AnswerType.FAULTED;
			} else {
				scoreCount++;
				answers[currentWordIndex] = AnswerType.CORRECT;
			}

			nextWord(event);
		} else {
			// user got the word wrong
			if (attemptNumber == 1 && mode == Mode.PRACTICE) {
				// If user has only gotten it wrong once, read the word again and wait for answer
				// we only do this in practice mode.
				attemptNumber = 2;

				// Gives a hint depending on the answer
				String hintPrefix = correctness == Answer.Correctness.ONLY_MACRONS_WRONG
					? "Almost right! Check the macrons. "
					: correctness == Answer.Correctness.ONLY_SYNTAX_WRONG
						? "Almost right! Check your spaces and hyphens. "
						: "Incorrect, try once more. "; // if totally wrong

				statusLabel.setText(
					hintPrefix +
					" Hint: The first and last letters are '" +
					correctAnswer.charAt(0) +
					"' and '" +
					correctAnswer.charAt(correctAnswer.length() - 1) +
					"', and the English word is '" +
					words[currentWordIndex].english +
					"'."
				);

				answerField.clear();
			} else {
				// User has gotten it wrong twice in practice mode, or once in game mode.
				// Writes encouraging message
				statusLabel.setText(
					"Incorrect. Chin up, you've got the next one!" +
					(
						mode == Mode.PRACTICE
							? "\nThe correct spelling was: " + correctAnswer
							: ""
					)
				);
				answers[currentWordIndex] = AnswerType.INCORRECT;
				nextWord(event);
			}
		}
	}
}
