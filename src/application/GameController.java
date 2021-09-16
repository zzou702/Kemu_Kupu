package application;

import java.io.File;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import application.helpers.*;

public class GameController {
	private Stage stage; //Declaration of core UI components
	private Scene scene;
	private Parent root;

	private int wordCountCurrent = 1; //Declares variables and file paths
	private int wordCountTotal;
	private int buttonEnableCount = 0;

	private String prevWord = "999";
	private String uniqueFile = "src/words/.new";
	private String uniqueFileTemp = "src/words/.new.tmp";
	private String gameState = "a";
	private String reviewFile = "src/words/.failed";
	private String reviewFileTemp = "src/words/.failed.tmp";
	private String statsFile = "src/words/.stats";
	private String faultedFile = "src/words/.faulted";
	private String masteredFile = "src/words/.mastered";

	@FXML //Declares widgets created in SceneBuilder
	private Label statusLabel;
	@FXML
	private TextField answerField;
	@FXML
	private Label countLabel;
	@FXML
	private Label quizTitle;
	@FXML
	private Button homeButton;

	public void displayCount(int wordCount) { //Method that sets the labels based on word count

		countLabel.setText("Spell word " + Integer.toString(wordCountCurrent) + " of " + Integer.toString(wordCount));
		wordCountTotal = wordCount;
	}

	public void displayTitle(String name) { //Method that changes the title based on the quiz type

		quizTitle.setText("New " + name + " Quiz");
		gameState = name;
	}


	public void switchHome(ActionEvent e) throws IOException { //Switches back to home screen on button press

		root = FXMLLoader.load(getClass().getResource("A2Doc.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void readWord() { //Reads word 

		try {
			File testWords = new File(uniqueFile); 
			Scanner reader = new Scanner(testWords);

			if (reader.hasNextLine()) {
				String word = reader.nextLine();
				String readWord = "echo " + word + " | festival --tts"; //Passes bash command into process builder
				Bash.exec(readWord);
			}

			reader.close();
		} catch (Exception f) {
			f.printStackTrace();
		}
	}

	public void deleteWord() { 

		try {
			String delWord = "tail -n +2 " + uniqueFile + " > " + uniqueFileTemp + " && mv " + uniqueFileTemp + " " + uniqueFile; //Deletes the first word of the file
			Bash.exec(delWord);
			answerField.clear();
		} catch (Exception f) {
			f.printStackTrace();
		}
	}

	public void submit(ActionEvent e) {

		try {
			statusLabel.setText("");
			String answer = answerField.getText().toLowerCase().strip(); //Gets the value in the text field
			File testWords = new File(uniqueFile);
			Scanner reader = new Scanner(testWords);

			if (reader.hasNextLine()) {
				String word = reader.nextLine();

				if (answer.equals(word)) { //Compares the answer with the value from text field, and reads out the correctness
					statusLabel.setText("Correct!");
					String correctMsg = "echo Correct! | festival --tts";
					Bash.exec(correctMsg);

					if (prevWord.equals(word)) { //If the word is faulted, it adds the word to a file with other faulted words, and also records to statistics file
						String faulted = "echo " + word + " >> " + faultedFile;
						String faultedStats = "echo " + word + " faulted >> " + statsFile;
						Bash.exec(faulted);
						Bash.exec(faultedStats);
					}

					else if (!prevWord.equals(word)){ //If the word is mastered, it adds the word to a file with other mastered words, and also records to statistics file
						String mastered = "echo " + word + " >> " + masteredFile;
						String masteredStats = "echo " + word + " mastered >> " + statsFile;
						Bash.exec(mastered);
						Bash.exec(masteredStats);

					}

					deleteWord(); //Deletes the word from the file of words currently being tested

					if (wordCountCurrent < wordCountTotal) { //Increments the current word count
						wordCountCurrent++;
					}

					buttonEnableCount++;

					if (gameState.equals("Review")) { //Only applies to when the game mode is review
						int lines = 0;
						String failedUrl = "src/words/.failed";
						File fileName = new File(failedUrl);

						try (BufferedReader lineRead = new BufferedReader(new FileReader(fileName))) { //Counts the number of lines in the failed file
							while (lineRead.readLine() != null) lines++;
						} catch (IOException g) {
							g.printStackTrace();
						}

						if (lines == 1) { //Deletes the file if there is only one word left 
							String removeFailedFile = "rm -f " + reviewFile;
							Bash.exec(removeFailedFile);
						}

						else { //Removes the word from the failed file if user got it correct
							String removeFromFailed = "grep -vw " + word + " " + reviewFile + " > " + reviewFileTemp + " && mv " + reviewFileTemp + " " + reviewFile;
							Bash.exec(removeFromFailed);
						}
					}

					displayCount(wordCountTotal); //Updates the word count label
					readWord();
				}

				else {

					if (!prevWord.equals(word)) { //If user has only gotten it wrong once, read the word again and wait for answer
						prevWord = word;
						statusLabel.setText("Incorrect, try once more");
						String readWord = "echo Incorrect, try once more " + word + ", " +  word + " | festival --tts";
						Bash.exec(readWord);
						answerField.clear();
					}

					else {
						statusLabel.setText("Incorrect");
						String incorrect = "echo Incorrect | festival --tts";
						Bash.exec(incorrect);
						deleteWord();

						if (wordCountCurrent < wordCountTotal) {
							wordCountCurrent++;
						}

						buttonEnableCount++;
						displayCount(wordCountTotal); //Updates word count label
						readWord();
						prevWord = "999";

						String failed = "echo " + word + " >> " + reviewFile; //Adds the failed word onto the failed file
						String failedStats = "echo " + word + " failed >> " + statsFile;
						Bash.exec(failed);
						Bash.exec(failedStats);
					}
				}
			}

			reader.close();

			if (buttonEnableCount == wordCountTotal) { //Unlocks home button when the test is finished
				homeButton.setDisable(false);
			}
		} catch (Exception f) {
			f.printStackTrace();
		}
	}
}
