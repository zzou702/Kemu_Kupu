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
	@FXML
	private Button skipButton;

	public void displayCount(int wordCount) { //Method that sets the labels based on word count

		countLabel.setText("Spell word " + Integer.toString(wordCountCurrent) + " of " + Integer.toString(wordCount));
		wordCountTotal = wordCount;
	}

	public void displayTitle(String name) { //Method that changes the title based on the quiz type

		quizTitle.setText("New " + name + " Quiz");
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
				Festival.speak(word, Festival.Language.ENGLISH);
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
	
	public void skipWord(ActionEvent e) {//Deletes word and reads out the next word, incrementing the word number
		deleteWord();
		
		if (wordCountCurrent < wordCountTotal) { //Increments the current word count
			wordCountCurrent++;
		}
		buttonEnableCount++;
		displayCount(wordCountTotal);
		
		readWord();
		
	}
	
	public void repeatWord(ActionEvent e) { //Repeats the word on button press
		readWord();
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
					Festival.speak("Correct!", Festival.Language.ENGLISH);

					deleteWord(); //Deletes the word from the file of words currently being tested

					if (wordCountCurrent < wordCountTotal) { //Increments the current word count
						wordCountCurrent++;
					}

					buttonEnableCount++;


					displayCount(wordCountTotal); //Updates the word count label
					readWord();
				}

				else {

					if (!prevWord.equals(word)) { //If user has only gotten it wrong once, read the word again and wait for answer
						prevWord = word;
						statusLabel.setText("Incorrect, try once more. Hint: " + word.charAt(1));
						String readWord = "echo Incorrect, try once more " + word + ", " +  word;
						Festival.speak(readWord, Festival.Language.ENGLISH);
						answerField.clear();
					}

					else {
						statusLabel.setText("Incorrect");
						Festival.speak("Incorrect", Festival.Language.ENGLISH);
						deleteWord();

						if (wordCountCurrent < wordCountTotal) {
							wordCountCurrent++;
						}

						buttonEnableCount++;
						displayCount(wordCountTotal); //Updates word count label
						readWord();
						prevWord = "999";
						
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
