package application;

import application.helpers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Game extends MainContext {

	/** the random words for this quiz */
	private Topics.Word[] words;

	/** the current question number, starting at 0 */
	private int currentWordIndex = 0;

	/** 1 if this is first attempt at this word. 2 if it is the second attempt */
	private int attemptNumber = 1;

	/** the current score, initially 0 **/
	private double scoreCount = 0;

	@FXML //Declares widgets created in SceneBuilder
	private Label statusLabel;

	@FXML
	private TextField answerField;

	@FXML
	private Label countLabel;

	@FXML
	private Label quizTitle;

	@FXML
	private Label scoreLabel;

	/** called by the topic selection page  */
	public void startGame(Topics.Topic topic) throws Exception {
		quizTitle.setText("New Quiz: " + topic.title);
		words = topic.getRandomWords();
		scoreLabel.setText("Score: " + scoreCount);

		this.speakCurrentWord();
		this.refreshUI();
	}

	/** this method updates the UI at the start of each question */
	private void refreshUI() {
		countLabel.setText(
			"Spell word " +
			Integer.toString(currentWordIndex + 1) +
			" of " +
			Integer.toString(words.length)
		);

		// we also print the answer to the console to aid debugging
		System.out.println("Correct answer: " + words[currentWordIndex].teReo);
	}

	public void switchHome(ActionEvent e) { //Switches back to home screen on button press
		this.navigateTo("Home.fxml", e);
	}

	/** called by the repeat button, and also by other methods */
	public void speakCurrentWord() {
		Festival.speak(words[currentWordIndex].teReo, Festival.Language.TE_REO);
	}

	/** called by the skip button, and also by other methods */
	public void nextWord(ActionEvent e) {
		// go to the next word, and reset the number of attempts for this word
		currentWordIndex++;
		attemptNumber = 1;
		answerField.clear();

		if (currentWordIndex == words.length) {
			// we are now done
			Reward rewardPage = (Reward) this.navigateTo("Reward.fxml", e);
			rewardPage.setScore(scoreCount);
			return;
		}

		// if we get to here, the game is not over.
		// so move to the next question
		this.speakCurrentWord();
		this.refreshUI();
	}

	/** called when you click the submit button */
	public void submit(ActionEvent e) {
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
			Festival.speak("Correct!", Festival.Language.ENGLISH);
			//If answered correctly on second attempt, add half point. Otherwise add full point
			if (attemptNumber == 2) {
				scoreCount += 0.5;
			} else {
				scoreCount++;
			}

			scoreLabel.setText("Score: " + scoreCount);
			nextWord(e);
		} else {
			// user got the word wrong
			if (attemptNumber == 1) { //If user has only gotten it wrong once, read the word again and wait for answer
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
					"'"
				);
				Festival.speak(
					"Incorrect, try once more " + correctAnswer + ", " + correctAnswer,
					Festival.Language.ENGLISH
				);
				answerField.clear();
			} else {
				// user has gotten it wrong twice
				statusLabel.setText("Incorrect");
				Festival.speak("Incorrect", Festival.Language.ENGLISH);
				nextWord(e);
			}
		}
	}
}
