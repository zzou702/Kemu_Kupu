package application;

import application.helpers.*;
import application.wrappers.Dropdown;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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

	/** represents the type of quiz that's being played (practice or game) */
	public enum Mode {
		PRACTICE,
		GAME,
	}

	/** represents the type of answer that the user provided for a question */
	public enum AnswerType {
		CORRECT,
		FAULTED, // this means they got it incorrect on the first attempt, then correct. Only possible in practice mode
		INCORRECT,
		SKIPPED,
		TOO_SLOW, // this means they didn't answer within the time limit
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

	/** the number of seconds since the current question began */
	private int clock;

	/** the maximum number of seconds allowed per question */
	private static int TIME_LIMIT = 120;

	/** the location of the cursor within the textfield, before a macron button is pushed */
	private int oldCaretPosition;

	/** `true` if we are currently transitioning from one question to the next */
	private boolean isSubmitting = false;

	private Timeline timeline;

	@FXML
	private Label statusLabel, countLabel, quizTitle, scoreLabel, underscoreHintLabel, timeLabel;

	@FXML
	private TextField answerField;

	@FXML
	private ProgressBar timeBar;

	@FXML
	private Button backButton, skipButton, repeatButton, submitButton, helpButton;

	@FXML
	private AnchorPane gamePane;

	@FXML
	private ComboBox<String> ttsSpeedDropdown;

	/** This method inserts a vowel with a macron on button press. This method is used by 5 buttons **/
	public void insertMacron(ActionEvent event) {
		/** the character with the macron */
		String character = (String) ((Node) event.getSource()).getUserData();
		answerField.insertText(oldCaretPosition, character);

		// move the cursor back to the textField and re-focus on it.
		// the allows the user to resume typing immediately.
		answerField.requestFocus();
		answerField.positionCaret(oldCaretPosition + 1);
	}

	/** called by the topic selection page when it renders this page */
	public void startGame(Topics.Topic topic, Mode mode) throws Exception {
		FX.fadeIn(gamePane);
		this.mode = mode;

		initGameTTSDropDown();
		quizTitle.setText(topic.title);
		backButton.setText(text("back"));
		skipButton.setText(text("skip"));
		submitButton.setText(text("submit"));
		repeatButton.setText(text("repeat"));
		helpButton.setText(text("help"));

		words = topic.getRandomWords();

		this.speakCurrentWord();
		this.refreshUI();

		if (mode == Mode.GAME) {
			timeBar.setVisible(true);
			timeLabel.setVisible(true);
		}

		//Checks for changes in caret position of text field.
		//Source: https://stackoverflow.com/questions/36054363/is-there-more-than-one-caret-in-javafx-textfield
		answerField
			.focusedProperty()
			.addListener((observable, oldValue, newValue) -> {
				if (!newValue) {
					oldCaretPosition = answerField.getCaretPosition();
				}
			});
	}

	// Initializes dropdown for festival speeds in the game window.
	private void initGameTTSDropDown() {
		new Dropdown<Double>(
			/* element */ttsSpeedDropdown,
			/* values */FXCollections.observableArrayList(
				text("ttsSpeed_fast"),
				text("ttsSpeed_normal"),
				text("ttsSpeed_slow"),
				text("ttsSpeed_verySlow")
			),
			/* keys */new Double[] { 0.5, 1.0, 1.5, 2.0 },
			/* initial value */context.getTTSSpeed()
		) {
			public void onChange(Double newValue) {
				context.setTTSSpeed(newValue);
			}
		};
	}

	/** the "underscore hint" is the text that says "P _ _ _ a   _ _ " or the correct spelling of the word  */
	private void updateUnderscoreHint(AnswerType type) {
		switch (type) {
			case FAULTED:
				underscoreHintLabel.setText(
					Format.getUnderscoreHint(
						words[currentWordIndex].teReo,
						attemptNumber == 2,
						false
					)
				);
				break;
			case INCORRECT:
				underscoreHintLabel.setText(
					Format.getUnderscoreHint(words[currentWordIndex].teReo, false, true)
				);
				break;
			default:
				underscoreHintLabel.setText(
					Format.getUnderscoreHint(words[currentWordIndex].teReo, false, false)
				);
				break;
		}
	}

	/** this method updates the UI at the start of each question */
	private void refreshUI() {
		countLabel.setText(text("spellWord", currentWordIndex + 1, words.length));

		//Chooses the default case in the switch case
		updateUnderscoreHint(AnswerType.SKIPPED);

		scoreLabel.setText(text("scoreDisplay", Format.formatScore(scoreCount)));

		// When in game mode, creates a timer, counting up once per second, while decreasing the progress bar.
		if (mode == Mode.GAME) {
			clock = 0;

			if (timeline != null) {
				timeline.stop();
			}

			// Initialize the clock to show the time limit
			timeLabel.setText(text("time", Format.formatAsTime(TIME_LIMIT)));

			timeline =
				new Timeline(
					new KeyFrame(
						Duration.seconds(1),
						(ActionEvent event) -> {
							// this runs every second, and updates the progress bar and clock text
							clock++;
							timeLabel.setText(
								text("time", Format.formatAsTime(TIME_LIMIT - clock))
							);
							timeBar.setProgress(1 - (double) clock / TIME_LIMIT);

							if (clock == TIME_LIMIT) {
								// stop, time is up. The question will be marked as wrong
								timeline.stop();
								statusLabel.setText(text("tooSlow"));
								nextWord(AnswerType.TOO_SLOW);
							}
						}
					)
				);

			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.playFromStart();
		}
	}

	// Called when help button is pressed
	public void help(ActionEvent event) {
		Help.showPopup(Help.Category.GAME);
	}

	//Stops the countdown when exiting to different scene.
	private void stopCountdown() {
		if (mode == Mode.GAME) {
			timeline.stop();
		}
		Festival.emptyQueue();
	}

	/** called by the back button */
	public void switchHome(ActionEvent event) {
		backButton.setDisable(true);
		stopCountdown();
		FX
			.fadeOut(gamePane)
			.setOnFinished(e -> {
				this.navigateTo("Home.fxml", event);
			});
	}

	/** called by the repeat button, and also by other methods */
	public void speakCurrentWord() {
		answerField.requestFocus();
		// Disables buttons while festival is speaking in a separate thread
		backButton.setDisable(true);
		repeatButton.setDisable(true);

		Festival.speak(
			words[currentWordIndex].teReo,
			Festival.Language.TE_REO,
			this.context.getTTSSpeed(),
			() -> {
				// re-enable buttons when finished speaking
				backButton.setDisable(false);
				repeatButton.setDisable(false);
			}
		);
	}

	/**
	 * goes to the next word after the user passed, failed, or skipped the previous word
	 * You must supply an argument which is the AnswerType of the last question.
	 */
	public void nextWord(AnswerType lastAnswer) {
		answers[currentWordIndex] = lastAnswer;

		// remove any queued words from the Festival queue immediately
		Festival.emptyQueue();

		currentWordIndex++;
		attemptNumber = 1; // reset the number of attempts, since this is a new word
		answerField.clear();

		// check if we just completed the final word in the quiz
		if (currentWordIndex == words.length) {
			// we are now done
			stopCountdown();

			FX
				.fadeOut(gamePane)
				.setOnFinished(e -> {
					Reward rewardPage = (Reward) this.navigateTo(
							"Reward.fxml",
							statusLabel
						);
					rewardPage.initialize(scoreCount, answers, words, mode);
				});

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
		isSubmitting = false;
	}

	/** called by the skip button and when the time runs out (in game mode) */
	public void skipWord(ActionEvent event) {
		if (currentWordIndex == words.length) return; // do nothing if the user is spamming this button

		// Writes encouraging message
		statusLabel.setText(text("skippedMsg"));
		nextWord(AnswerType.SKIPPED);
	}

	/** called when you click the submit button */
	public void submit(ActionEvent event) {
		// if the user presses enter multiple times, ignore subsequent presses
		if (isSubmitting) return;
		isSubmitting = true;

		if (currentWordIndex == words.length) return; // do nothing if the user is spamming this button

		statusLabel.setText("");
		String usersAnswer = answerField.getText(); // Gets the value in the text field
		String correctAnswer = words[currentWordIndex].teReo;
		double userTime = timeBar.getProgress();

		Answer.Correctness correctness = Answer.checkAnswer(
			usersAnswer,
			correctAnswer
		);

		if (correctness == Answer.Correctness.CORRECT) {
			// Answer is completely correct
			statusLabel.setText(text("correct"));

			// If answered correctly on second attempt, add half point. Otherwise add full point
			if (attemptNumber == 2) {
				scoreCount += 0.5;
				nextWord(AnswerType.FAULTED);
			} else {
				scoreCount++;
				// in game mode, you get a bonus point depending on how long you took.
				// for example, if >75% of the time is remaining, you get the entire bonus point
				if (mode == Mode.GAME) {
					if (userTime >= 0.75) {
						scoreCount++;
					} else if (userTime >= 0.5 && userTime < 0.75) {
						scoreCount += 0.75;
					} else if (userTime >= 0.25 && userTime < 0.5) {
						scoreCount += 0.5;
					} else if (userTime >= 0 && userTime < 0.25) {
						scoreCount += 0.25;
					}
				}
				nextWord(AnswerType.CORRECT);
			}
			FX.flashElement(answerField, FX.State.SUCCESS);
		} else {
			// user got the word wrong
			if (attemptNumber == 1 && mode == Mode.PRACTICE) {
				// If user has only gotten it wrong once, read the word again and wait for answer
				// we only do this in practice mode.
				attemptNumber = 2;

				// Gives a hint depending on the answer
				String hintPrefix = correctness == Answer.Correctness.ONLY_MACRONS_WRONG
					? text("hintMacrons")
					: correctness == Answer.Correctness.ONLY_SYNTAX_WRONG
						? text("hintHyphens")
						: text("hintGeneric"); // if totally wrong

				statusLabel.setText(
					hintPrefix +
					"\n" +
					text("englishWordIs", words[currentWordIndex].english)
				);

				//Displays two letter hint.
				updateUnderscoreHint(AnswerType.FAULTED);
				answerField.clear();

				//Disables submit button and flashes the status
				submitButton.setDisable(true);
				FX
					.flashElement(answerField, FX.State.WARNING)
					.setOnFinished(e -> {
						submitButton.setDisable(false);
						isSubmitting = false;
					});
			} else {
				// User has gotten it wrong twice in practice mode, or once in game mode.
				// Writes encouraging message
				statusLabel.setText(text("incorrect"));

				//Displays correct spelling of word.
				if (mode == Mode.PRACTICE) {
					updateUnderscoreHint(AnswerType.INCORRECT);
				}

				//Disables submit button and flashes the status
				submitButton.setDisable(true);

				FX
					.flashElement(answerField, FX.State.DANGER)
					.setOnFinished(e -> {
						submitButton.setDisable(false);
						nextWord(AnswerType.INCORRECT);
					});
			}
		}
	}
}
